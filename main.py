# Main application entry point

from flask import Flask
from config import API_ENDPOINT

app = Flask(__name__)

if __name__ == '__main__':
    app.run(debug=True, port=5002)