from flask import Flask, request, jsonify
import os
import requests

load_dotenv()
app = Flask(__name__)

# Discord webhook URL
DISCORD_WEBHOOK_URL = 'https://discord.com/api/webhooks/YOURDISCORDWEBHOOK'

@app.route('/upload', methods=['POST'])
def upload_file():
    if 'image' not in request.files:
        return jsonify({'error': 'No file part'}), 400
    file = request.files['image']
    if file.filename == '':
        return jsonify({'error': 'No selected file'}), 400
    if file:
        file_path = os.path.join('/tmp', file.filename)
        file.save(file_path)
        upload_to_discord(file_path)
        return jsonify({'message': 'File uploaded successfully', 'file_path': file_path}), 200

def upload_to_discord(file_path):
    with open(file_path, 'rb') as f:
        response = requests.post(
            DISCORD_WEBHOOK_URL,
            files={'file': f}
        )
    os.remove(file_path)
    return response

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)