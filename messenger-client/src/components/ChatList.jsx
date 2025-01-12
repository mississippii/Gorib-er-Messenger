import ChatItem from './ChatItem';

const ChatList = ({ users, onSelectUser, selectedUser }) => {
  // Map the users array to prepare chat items for display
  const chats = users?.map((user) => ({
    name: user.name, // Display the username
    ip: user.ip, // Keep the IP for functionality
    message: '........',
    time: '3:41 PM',
    avatar: 'https://via.placeholder.com/50', // Placeholder avatar
    isActive: selectedUser === user.ip, // Highlight if selected
  }));

  return (
    <div className="w-[80%] flex flex-col gap-2">
      {chats.map((chat, index) => (
        <ChatItem
          key={index}
          name={chat.name} // Pass the username for display
          message={chat.message}
          time={chat.time}
          avatar={chat.avatar}
          isActive={chat.isActive} // Highlight if this is the selected user
          onSelectUser={onSelectUser}
          userIp={chat.ip}
        />
      ))}
    </div>
  );
};

export default ChatList;
