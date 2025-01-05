// src/components/ChatWindow.js
import React from "react";
import "../css/ChatWindow.css";

const ChatWindow = ({ messages, selectedUser }) => {
  return (
    <div className="chat-window">
      <header className="chat-header">{selectedUser ? selectedUser.name : "Select a user"}</header>
      <div className="chat-messages">
        {messages.map((msg, index) => (
          <div
            key={index}
            className={`message ${msg.sender === "me" ? "outgoing" : "incoming"}`}
          >
            {msg.text}
          </div>
        ))}
      </div>
    </div>
  );
};

export default ChatWindow;
