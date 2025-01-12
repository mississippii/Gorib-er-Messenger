const ChatItem = ({
  name,
  message,
  time,
  avatar,
  isActive,
  userIp,
  onSelectUser,
}) => {
  return (
    <div
      className={`flex py-2 px-3 items-center gap-2 rounded-lg cursor-pointer shadow-md transition min-w-full ${
        isActive ? 'bg-blue-100' : 'hover:bg-gray-100'
      }`}
      onClick={() => onSelectUser(userIp)}
    >
      {/* Avatar */}
      <img src={avatar} alt="avatar" className="w-10 h-10 rounded-full" />

      {/* User info */}
      <div className="flex-grow">
        <h2 className="font-semibold text-gray-800 text-sm">{name}</h2>
        <p className="text-sm text-gray-600 truncate">{message}</p>
      </div>

      {/* Time */}
      <span className="text-xs text-gray-500 whitespace-nowrap">{time}</span>
    </div>
  );
};

export default ChatItem;
