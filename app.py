from flask import Flask, request, jsonify
from flask_cors import CORS
from transformers import pipeline, AutoModelForCausalLM, AutoTokenizer
from models.chatbot_model import get_chatbot_response 
import logging

app = Flask(__name__)
CORS(app, origins=["http://localhost:8080"])  # Specify allowed origin

# Configure logging
logging.basicConfig(level=logging.DEBUG)

@app.route('/chat', methods=['POST'])
def chatbot_response():
    app.logger.info('Received request for /chat')
    try:
        # Debug print to check what is received
        print("Received JSON:", request.json)
        user_message = request.json['message']
        response = get_chatbot_response(user_message)
        return jsonify({'response': response})
    except KeyError as e:
        # KeyError for when 'message' key is not found in JSON
        return jsonify({'error': f'Missing key in JSON data: {str(e)}'}), 400
    except Exception as e:
        app.logger.error(f'Error occurred: {str(e)}', exc_info=True)
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(port=5001)
