# PixTransfer
![GitHub stars](https://img.shields.io/github/stars/EtienneFo/PixTransfer?style=social)
![GitHub license](https://img.shields.io/github/license/EtienneFo/PixTransfer)

## Description

This project consists of a Flask server that allows users to upload images. The server provides two main functionalities:
1. Downloading images to a specified directory on the server.
2. Uploading images to a Discord server using a webhook.

## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/EtienneFo/your-repo.git
    ```
2. Navigate to the project directory:
    ```sh
    cd your-repo
    ```
3. Set up the Flask server:
    ```sh
    cd FlaskServer
    python -m venv .venv
    ```
4. Activate the virtual environment:
    - **Linux/macOS**:
      ```sh
      source .venv/bin/activate
      ```
    - **Windows**:
      ```sh
      .venv\Scripts\activate
      ```
5. Install dependencies:
    ```sh
    pip install -r requirements.txt
    ```
6. Run the Flask server to download images on your server:
    ```sh
    python app.py
    ```
7. Run the Flask server if you want to upload images to a Discord server:
    ```sh
    python discord.py
    ```

## Configuration

Before running the server, you need to configure the following:


1. **Server IP** Modify `network_security_config.xml` and `GestionImage` to define the IP address of the server.
2. **Download directory**: Modify `config.py` to define the folder where images will be stored.
3. **Discord Webhook URL**: Add your webhook link in `discord.py`:

       ```python
       DISCORD_WEBHOOK_URL = "https://discord.com/api/webhooks/..."
       ```

## Usage

Launch the Flask server, then launch the app and click on the "Upload" button to upload an image. The image will be downloaded to the specified directory on the server or channel if you use the discord webhook.

## Disclaimer

This application is provided "as is" without any guarantees or warranty. In association with the product, the developer makes no warranties of any kind, either express or implied, including but not limited to warranties of merchantability, fitness for a particular purpose, of title, or of non-infringement of third-party rights. Use of the product by a user is at the user’s risk.

The developer is not responsible for any damages or legal issues that may arise from the use of this application. It is the user's responsibility to ensure that the application is used in a legal and ethical manner.

## License

MIT License

Copyright (c) 2025 EtienneFo
