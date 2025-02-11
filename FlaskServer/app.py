from flask import Flask, request, jsonify
import os

load_dotenv()
app = Flask(__name__)

# Directory where images will be saved
UPLOAD_FOLDER = r'save/folder/path'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)

@app.route('/upload', methods=['POST'])
def upload_file():
    if 'image' not in request.files:
        return jsonify({'error': 'No image part in the request'}), 400

    file = request.files['image']
    if file.filename == '':
        return jsonify({'error': 'No selected file'}), 400

    if file:
        file_path = os.path.join(UPLOAD_FOLDER, file.filename)
        file.save(file_path)
        return jsonify({'message': 'File successfully uploaded'}), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)