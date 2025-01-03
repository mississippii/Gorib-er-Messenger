import React, { useState, useEffect, useRef } from 'react';
import './css/App.css'; // Import CSS for styling

function App() {
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);
  const [ws, setWs] = useState(null);
  const messageBoxRef = useRef(null);
  const [selectedReceiver, setSelectedReceiver] = useState(null);
  const [activeUsers, setActiveUsers] = useState([]);


  useEffect(() => {
    const socket = new WebSocket('ws://192.168.0.132:8880/chat');
  setWs(socket);

  socket.onopen = () => {
    console.log('Connected to WebSocket');
  };

    // Handle incoming messages from the server
    socket.onmessage = (event) => {
      const incomingMessage = JSON.parse(event.data);
      console.log('Incoming message:', incomingMessage);
      switch (incomingMessage.type) {
        case 'users':
          const userList = incomingMessage.userList;
          const mappedUsers = Object.keys(userList); // We are only interested in IP:port
          setActiveUsers(mappedUsers); // Set active users state
          console.log('Active users:', mappedUsers);
          break;
        case 'message':
          setMessages((prevMessages) => [
            ...prevMessages,
            { sender: incomingMessage.sender, text: incomingMessage.text },
          ]);
          break;
        case 'video':
        case 'audio':
          console.log("Don't Receive video/audio data");
          break;
        default:
          console.log('Unknown message type:', incomingMessage.type);
      }
    };

    // Handle errors
    socket.onerror = (error) => {
      console.error('WebSocket error: ', error);
    };

    // Handle WebSocket closure
    socket.onclose = () => {
      console.log('WebSocket connection closed');
    };

    // Set WebSocket connection state
    setWs(socket);

    // Cleanup WebSocket connection when component is unmounted
    return () => {
      if (socket) {
        socket.close();
      }
    };
  }, []);

  const sendMessage = () => {
    if (ws && message.trim() && selectedReceiver) {
      const sender = ''; // Hardcoded or dynamically set sender IP and port
      const receiver = selectedReceiver; // Use the selected receiver from the UI

      const messageData = {
        type: 'message',
        sender: sender,
        receiver: receiver,
        text: message,
      };

      ws.send(JSON.stringify(messageData));

      setMessages((prevMessages) => [
        ...prevMessages,
        { sender: 'You', text: message, isSender: true },
      ]);

      setMessage('');
      messageBoxRef.current.focus();
    } else {
      alert('Please select a receiver before sending a message.');
    }
  };

  const handleReceiverSelect = (receiverId) => {
    setSelectedReceiver(receiverId); // Set the selected receiver's IP:port
    setMessages([]); // Clear the message box when switching users
  };

  return (
    <div className="App">
      <h1>WebSocket Chat</h1>

      <div className="chat-container">
        {/* Active Users List */}
        <div className="users-list">
          <h3>Active Users</h3>
          <ul>
            {activeUsers.map((user) => (
              <li
              key={user}
              onClick={() => handleReceiverSelect(user)}
              style={{
                cursor: 'pointer',
                backgroundColor: selectedReceiver === user ? '#ddd' : 'transparent',
                padding: '5px',
              }}>
              {user} {/* Display only the IP:port */}
            </li>
            ))}
          </ul>
        </div>

        {/* Message Box */}
        <div className="message-box">
          {messages.map((msg, index) => (
            <div
              key={index}
              className={`message ${msg.isSender ? 'sent' : 'received'}`}
            >
              <strong>{msg.sender}:</strong> {msg.text}
            </div>
          ))}
        </div>

        {/* Message Input */}
        <div className="input-area">
          <input
            type="text"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            ref={messageBoxRef}
            placeholder="Type your message"
            className="message-input"
          />
          <button onClick={sendMessage} className="send-button">Send</button>
        </div>
      </div>
    </div>
  );
}

export default App;
