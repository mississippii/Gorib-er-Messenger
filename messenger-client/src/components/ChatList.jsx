import ChatItem from './ChatItem';

const ChatList = ({ users, onSelectUser, selectedUser }) => {
  const chats = users?.map((user) => {
    return {
      name: user,
      message: '........',
      time: '3:41 PM',
      avatar: 'https://via.placeholder.com/50',
      isActive: false,
    };
  });

  return (
    <div className="flex flex-col gap-3 pt-4">
      {chats.map((chat, index) => (
        <ChatItem
          key={index}
          name={chat.name}
          message={chat.message}
          time={chat.time}
          avatar={chat.avatar}
          isActive={chat.isActive}
          onSelectUser={onSelectUser}
        />
      ))}
    </div>
  );
};

export default ChatList;
