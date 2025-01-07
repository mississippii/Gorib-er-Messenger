import React, { useEffect, useState } from 'react';
import '../css/Messenger.css';
import ChatWindow from './ChatWindow';
import MessageInput from './MessageInput';
import Sidebar from './Sidebar';

const Messenger = () => {
  const [messages, setMessages] = useState({}); // Stores all messages for all users
  const [socket, setSocket] = useState(null); // WebSocket connection
  const [activeUsers, setActiveUsers] = useState([]); // List of active users
  const [selectedUser, setSelectedUser] = useState(null); // Currently selected user for chat

  // Initialize WebSocket
  useEffect(() => {
    const ws = new WebSocket('ws://103.248.13.73:8880/chat');
    setSocket(ws);

    ws.onopen = () => {
      console.log('WebSocket connection opened');
    };

    ws.onmessage = (event) => {
      const incomingMessage = JSON.parse(event.data);

      switch (incomingMessage.type) {
        case 'users': {
          const userList = incomingMessage.userList;
          const newActiveUsers = Object.keys(userList);

          // Preserve messages for existing users and initialize new users
          setMessages((prevMessages) => {
            const updatedMessages = { ...prevMessages };

            newActiveUsers.forEach((user) => {
              if (!updatedMessages[user]) {
                updatedMessages[user] = []; // Initialize empty messages for new users
              }
            });

            return updatedMessages;
          });

          setActiveUsers(newActiveUsers);
          break;
        }

        case 'message': {
          const { sender, text, receiver } = incomingMessage;

          setMessages((prevMessages) => ({
            ...prevMessages,
            [sender]: [
              ...(prevMessages[sender] || []),
              { sender, text, receiver },
            ],
          }));
          break;
        }

        case 'registration':
        case 'audio':
          console.log("Don't Receive video/audio data");
          break;

        default:
          console.log('Unknown message type:', incomingMessage.type);
      }
    };

    ws.onclose = () => {
      console.log('WebSocket connection closed');
    };

    ws.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    return () => {
      console.log('Cleaning up WebSocket...');
      ws.close();
    };
  }, []);

  // Handle sending messages
  const sendMessage = (text) => {
    const message = {
      type: 'message',
      sender: '', // Add sender information (e.g., your username)
      receiver: selectedUser,
      text,
    };

    if (socket) {
      socket.send(JSON.stringify(message));
    }
  };

  // Update local message state and send message through WebSocket
  const handleSendMessage = (text) => {
    setMessages((prevMessages) => ({
      ...prevMessages,
      [selectedUser]: [
        ...(prevMessages[selectedUser] || []),
        { sender: 'me', text, receiver: selectedUser },
      ],
    }));
    sendMessage(text);
  };

  return (
    <div className="messenger">
      <Sidebar
        users={activeUsers} // Pass the updated user list
        selectedUser={selectedUser}
        onSelectUser={setSelectedUser}
      />
      {selectedUser && (
        <div className="main">
          <ChatWindow
            messages={messages[selectedUser] || []}
            selectedUser={selectedUser}
          />
          <MessageInput
            onSendMessage={handleSendMessage}
            sendMessage={sendMessage}
          />
        </div>
      )}
    </div>
  );
};

export default Messenger;
