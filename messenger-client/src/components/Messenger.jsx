import React, { useEffect, useState } from 'react';
import { Toaster, toast } from 'react-hot-toast';
import ChatWindow from './ChatWindow';
import LoginPage from './LoginPage';
import MessageInput from './MessageInput';
import UserList from './UserList';

const Messenger = () => {
  const [messages, setMessages] = useState({}); // Stores all messages for all users
  const [socket, setSocket] = useState(null); // WebSocket connection
  const [activeUsers, setActiveUsers] = useState([]); // List of active users with names and IPs
  const [selectedUser, setSelectedUser] = useState(null); // Currently selected user for chat
  const [isApproved, setIsApproved] = useState(false);

  // Initialize WebSocket
  useEffect(() => {
    const ws = new WebSocket('ws://localhost:8880/chat');
    setSocket(ws);

    ws.onopen = () => {
      console.log('WebSocket connection opened');
    };

    ws.onmessage = (event) => {
      const incomingMessage = JSON.parse(event.data);

      switch (incomingMessage.type) {
        case 'users': {
          const userList = incomingMessage.userList; // Incoming data
          console.log(userList);

          const newActiveUsers = Object.keys(userList).map((ip) => ({
            ip, // Use IP for functionality
            name: userList[ip], // Extract name for display
          }));

          // Preserve messages for existing users and initialize new users
          setMessages((prevMessages) => {
            const updatedMessages = { ...prevMessages };

            newActiveUsers.forEach(({ ip }) => {
              if (!updatedMessages[ip]) {
                updatedMessages[ip] = []; // Initialize empty messages for new users
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

        case 'registration': {
          const { text } = incomingMessage;
          if (text === 'Approved') {
            setIsApproved(true);
            toast.success('Login successful!');
          } else {
            toast.error('Username already taken!');
          }
          break;
        }

        case 'audio': {
          console.log("Don't Receive video/audio data");
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

  const sendLoginDetails = (formData) => {
    if (socket) {
      socket.send(
        JSON.stringify({
          type: 'registration',
          name: formData.name,
          password: null,
        })
      );
    }
  };

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
    <div className="h-screen w-[1500px] mx-auto">
      {isApproved ? (
        <div className="flex h-full">
          {/* User List */}
          <div className="basis-2/6 h-full">
            <UserList
              users={activeUsers} // Pass users with names and IPs
              selectedUser={selectedUser} // Currently selected user's IP
              onSelectUser={setSelectedUser} // Update selected user
            />
          </div>

          {/* Chat Window */}
          {selectedUser && (
            <div className="basis-4/6 flex flex-col justify-between h-full bg-white">
              <ChatWindow
                messages={messages[selectedUser] || []}
                selectedUser={selectedUser}
                users={activeUsers}
              />
              <MessageInput onSendMessage={handleSendMessage} />
            </div>
          )}
        </div>
      ) : (
        <LoginPage sendLoginDetails={sendLoginDetails} />
      )}

      <Toaster
        position="top-center"
        toastOptions={{
          duration: 3000,
        }}
      />
    </div>
  );
};

export default Messenger;
