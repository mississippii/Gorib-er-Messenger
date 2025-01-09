// src/components/MessageInput.js
import React, { useState } from 'react';

const MessageInput = ({ onSendMessage }) => {
  const [text, setText] = useState('');

  const handleSend = (e) => {
    e.preventDefault();
    if (text.trim()) {
      onSendMessage(text);
      setText('');
    }
  };

  return (
    <form className="flex gap-2 p-3 bg-[#fff]" onSubmit={(e) => handleSend(e)}>
      <input
        type="text"
        placeholder="Type a message"
        value={text}
        onChange={(e) => setText(e.target.value)}
        className="input input-bordered input-info w-full bg-white"
      />
      <button className="btn btn-info text-white">Send</button>
    </form>
  );
};

export default MessageInput;
