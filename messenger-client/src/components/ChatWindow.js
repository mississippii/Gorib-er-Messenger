// src/components/ChatWindow.js
import React from 'react';
import '../css/ChatWindow.css';

const ChatWindow = ({ messages, selectedUser }) => {
  return (
    <div className="chat-window">
      <header className="chat-header bg-[#fff] flex items-center justify-between px-4 py-3 shadow-xl">
        <div className="flex gap-2 ">
          <div className="avatar">
            <div className="w-14 rounded-full">
              <img
                src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ71Tc9Tk2q1eJUUlX1bXhWrc0-g8O9xnAplw&s"
                alt="user"
              />
            </div>
          </div>
          <div>
            <h6 className="text-xl">
              {selectedUser ? selectedUser : 'Select a user'}
            </h6>
            <div className="badge text-white bg-[#0D9276] border-none">
              Active
            </div>
          </div>
        </div>
        <div className="flex gap-4 text-xl">
          <i class="fa-solid fa-phone cursor-pointer"></i>
          <i class="fa-solid fa-video cursor-pointer"></i>
          <i class="fa-solid fa-ellipsis cursor-pointer"></i>
        </div>
      </header>
      <div className="chat-messages">
        {messages?.map((msg, index) => (
          <div
            className={`chat ${
              msg.sender === 'me' ? 'chat-end' : 'chat-start'
            }`}
          >
            <div
              className={`chat-bubble chat-bubble-info flex items-center px-3 py-1 text-sm min-h-0 shadow-md ${
                msg.sender !== 'me' ? 'bg-[#D4F6FF] text-[#000]' : 'text-white'
              }`}
            >
              {msg.text}
            </div>
          </div>
          // <div
          //   key={index}
          //   className={`message ${
          //     msg.sender === 'me' ? 'outgoing' : 'incoming'
          //   }`}
          // >
          //   {msg.text}
          // </div>
        ))}
      </div>
    </div>
  );
};

export default ChatWindow;
