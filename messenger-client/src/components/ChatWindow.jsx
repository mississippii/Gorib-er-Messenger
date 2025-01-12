import React from 'react';

const ChatWindow = ({ messages, selectedUser, users }) => {
  const activeUserName = users?.find((user) => user.ip === selectedUser).name;
  return (
    <div>
      <header className="chat-header flex items-center justify-between shadow-sm p-3">
        <div className="flex gap-2 ">
          <div className="avatar">
            <div className="w-10 h-10 rounded-full">
              <img
                src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ71Tc9Tk2q1eJUUlX1bXhWrc0-g8O9xnAplw&s"
                alt="user"
              />
            </div>
          </div>
          <div>
            <h6 className="text-xl">{activeUserName}</h6>
            <div className="rounded-full h-2 w-2 bg-green-500"></div>
          </div>
        </div>
        <div className="flex gap-4 text-md">
          <i class="fa-solid fa-phone cursor-pointer"></i>
          <i class="fa-solid fa-video cursor-pointer"></i>
          <i class="fa-solid fa-ellipsis cursor-pointer"></i>
        </div>
      </header>
      <div className="p-3">
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
        ))}
      </div>
    </div>
  );
};

export default ChatWindow;
