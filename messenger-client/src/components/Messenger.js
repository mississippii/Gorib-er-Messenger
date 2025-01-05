// src/components/Messenger.js
import React, { useState } from "react";
import Sidebar from "./Sidebar";
import ChatWindow from "./ChatWindow";
import MessageInput from "./MessageInput";
import "../css/Messenger.css"
const Messenger = () => {
  const [users] = useState([
    { id: 1, name: "Alice" },
    { id: 2, name: "Bob" },
    { id: 3, name: "Charlie" },
  ]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [messages, setMessages] = useState([]);

  const handleSendMessage = (text) => {
    setMessages((prev) => [...prev, { sender: "me", text }]);
  };

  return (
    <div className="messenger">
      <Sidebar
        users={users}
        selectedUser={selectedUser}
        onSelectUser={setSelectedUser}
      />
      <div className="main">
        <ChatWindow messages={messages} selectedUser={selectedUser} />
        <MessageInput onSendMessage={handleSendMessage} />
      </div>
    </div>
  );
};

export default Messenger;
