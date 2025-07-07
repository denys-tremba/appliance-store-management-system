document.addEventListener('DOMContentLoaded', () => {
    const chatForm = document.getElementById('chat-form');
    const messageInput = document.getElementById('message-input');
    const chatBox = document.getElementById('chat-box');
    const sendBtn = document.getElementById('send-btn');

    loadChatHistory();

    chatForm.addEventListener('submit', async function (e) {
        e.preventDefault(); // Prevent page reload

        const userInput = messageInput.value.trim();
        if (userInput === '') {
            return; // Don't send empty messages
        }

        // 1. Add user's message to the chat box
        appendMessage(userInput, 'request');

        // 2. Clear the input field
        messageInput.value = '';

        messageInput.disabled = true;
        sendBtn.disabled = true;

        try {
            // 3. Construct the URL and fetch data from your backend
            const query = encodeURIComponent(userInput);
            const url = `/chat/ai?prompt=${query}`;
            console.log(`Fetching from: ${url}`); // For debugging

            const response = await fetch(url);

            if (!response.ok) {
                // Handle HTTP errors like 404 or 500
                throw new Error(`Server responded with status: ${response.status}`);
            }

            const botText = await response.text();

            // 4. Add the backend's response to the chat box
            appendMessage(botText, 'response');

        } catch (error) {
            // 5. Handle potential network errors
            console.error("Fetch error:", error);
            appendMessage("Sorry, I'm having trouble connecting. Please try again later.", 'response');
        } finally {
            // 6. Re-enable the input field and button
            messageInput.disabled = false;
            sendBtn.disabled = false;
            messageInput.focus();
        }
    });

    function appendMessage(message, type) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('chat-message', type);
        messageElement.innerText = message;
        chatBox.appendChild(messageElement);

        // Scroll to the bottom of the chat box
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    // --- NEW: Function to fetch and display chat history ---
    async function loadChatHistory() {
        try {
            const response = await fetch('/chat/memory');
            if (!response.ok) {
                throw new Error(`History fetch failed with status: ${response.status}`);
            }
            const history = await response.json();

            // If history is empty or not an array, show a welcome message
            if (!Array.isArray(history) || history.length === 0) {
                appendMessage("Hello! How can I help you today?", 'response');
                return;
            }

            // Populate the chat box with history
            history.forEach((message, index) => {
                // Assuming even indices are user requests and odd are bot responses
                const type = (index % 2 === 0) ? 'request' : 'response';
                appendMessage(message, type);
            });

        } catch (error) {
            console.error("Could not load chat history:", error);
            // Provide a fallback welcome message if history fails to load
            appendMessage("Hello! How can I help you today?", 'response');
        }
    }
});