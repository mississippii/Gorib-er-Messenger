import React, { useState } from 'react';
import { Toaster, toast } from 'react-hot-toast';
import ChatWindow from './ChatWindow';
import LoginPage from './LoginPage';
import MessageInput from './MessageInput';
import UserList from './UserList';

const Messenger = () => {
  const [messages, setMessages] = useState({});
  const [socket, setSocket] = useState(null);
  const [activeUsers, setActiveUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [isApproved, setIsApproved] = useState(false);
  const [username, setUsername] = useState('');

  // Handle login and establish WebSocket connection
  const sendLoginDetails = (formData) => {
    const ws = new WebSocket('ws://localhost:8880/chat');

    ws.onopen = () => {
      console.log('WebSocket connection opened');
      // Send login details after connection opens
      ws.send(
          JSON.stringify({
            type: 'registration',
            name: formData.name,
            password: null,
          })
      );
    };

    ws.onmessage = (event) => {
      const incomingMessage = JSON.parse(event.data);

      switch (incomingMessage.type) {
        case 'users': {
          const userList = incomingMessage.userList;
          console.log(userList);

          const newActiveUsers = Object.keys(userList).map((ip) => ({
            ip,
            name: userList[ip],
          }));

          setMessages((prevMessages) => {
            const updatedMessages = { ...prevMessages };
            newActiveUsers.forEach(({ ip }) => {
              if (!updatedMessages[ip]) {
                updatedMessages[ip] = [];
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
            [sender]: [...(prevMessages[sender] || []), { sender, text, receiver }],
          }));
          break;
        }

        case 'registration': {
          const { text } = incomingMessage;
          if (text === 'Approved') {
            setIsApproved(true);
            setUsername(formData.name);
            toast.success('Login successful!');
          } else {
            toast.error('Username already taken!');
          }
          break;
        }

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

    // Save WebSocket instance
    setSocket(ws);
  };

  // Handle sending messages
  const sendMessage = (text) => {
    if (socket && selectedUser) {
      const message = {
        type: 'message',
        sender: username,
        receiver: selectedUser,
        text,
      };
      socket.send(JSON.stringify(message));

      setMessages((prevMessages) => ({
        ...prevMessages,
        [selectedUser]: [...(prevMessages[selectedUser] || []), { sender: 'me', text, receiver: selectedUser }],
      }));
    }
  };

  return (
      <div className="h-screen w-[1500px] mx-auto">
        {isApproved ? (
            <div className="flex h-full">
              {/* User List */}
              <div className="basis-2/6 h-full">
                <UserList users={activeUsers} selectedUser={selectedUser} onSelectUser={setSelectedUser} />
              </div>

              {/* Chat Window */}
              {selectedUser && (
                  <div className="basis-4/6 flex flex-col justify-between h-full bg-white">
                    <ChatWindow messages={messages[selectedUser] || []} selectedUser={selectedUser} users={activeUsers} />
                    <MessageInput onSendMessage={sendMessage} />
                  </div>
              )}
            </div>
        ) : (
            <LoginPage sendLoginDetails={sendLoginDetails} />
        )}

        <Toaster position="top-center" toastOptions={{ duration: 3000 }} />
      </div>
  );
};

export default Messenger;
