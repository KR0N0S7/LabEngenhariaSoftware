const chatContainer = document.getElementById('chat-container');
const messageInput = document.getElementById('message-input');
const sendButton = document.getElementById('send-button');

const CHATBOT_API_URL = "http://127.0.0.1:5001/chat"; // Replace with your API URL

sendButton.addEventListener('click', () => {
    const userMessage = messageInput.value;
    messageInput.value = ""; // Clear input field 

    displayMessage('user', userMessage); // Display user's message

    fetch(CHATBOT_API_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ message: userMessage })
    })
    .then(response => response.json())
    .then(data => displayMessage('bot', data.response)) // Display chatbot's response
    .catch(error => console.error('Error:', error)); 
});

function displayMessage(type, message) {
    const messageElement = document.createElement('div');
    messageElement.classList.add('message', type);
    messageElement.textContent = message;
    chatContainer.appendChild(messageElement); 
}

const closeButton = document.getElementById('close-chat');
closeButton.addEventListener('click', () => {
    document.querySelector('.chat-container').style.display = 'none'; 
});


