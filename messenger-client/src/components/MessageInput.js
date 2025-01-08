// src/components/MessageInput.js
import React, { useState } from 'react';
import '../css/MessageInput.css';

const MessageInput = ({ onSendMessage, sendMessage }) => {
  const [text, setText] = useState('');

  const handleSend = (e) => {
    e.preventDefault();
    if (text.trim()) {
      onSendMessage(text);
      sendMessage(text);
      setText('');
    }
  };

  return (
    <form className="flex gap-2 px-2 bg-[#fff]" onSubmit={(e) => handleSend(e)}>
      <input
        type="text"
        placeholder="Type a message"
        value={text}
        onChange={(e) => setText(e.target.value)}
        className="input input-bordered input-info w-full bg-white"
      />
      <button className="btn bg-[#1d232a] text-white ">Send</button>
    </form>
  );
};

export default MessageInput;
