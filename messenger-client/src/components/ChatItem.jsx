const ChatItem = ({ name, message, time, avatar, isActive, onSelectUser }) => {
  return (
    <div
      className={`flex items-center p-3 rounded-lg cursor-pointer ${
        isActive ? 'bg-gray-200' : 'hover:bg-gray-100'
      }`}
      onClick={() => onSelectUser(name)}
    >
      <img src={avatar} alt="avatar" className="w-12 h-12 rounded-full mr-3" />
      <div className="flex-grow">
        <h2 className="font-semibold">{name}</h2>
        <p className="text-sm text-gray-600 truncate">{message}</p>
      </div>
      <span className="text-xs text-gray-500">{time}</span>
    </div>
  );
};

export default ChatItem;