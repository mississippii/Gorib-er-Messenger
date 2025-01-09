import React from 'react';
import ChatList from './ChatList';
import SearchBar from './SearchBar';
import SideHeader from './SideHeader';

const UserList = ({ users, onSelectUser, selectedUser }) => {
  return (
    <div className="w-full h-full bg-white rounded-lg shadow-right flex flex-col p-1 py-4 gap-3 items-center">
      <SideHeader />
      <SearchBar />
      <ChatList
        users={users}
        onSelectUser={onSelectUser}
        selectedUser={users}
      />
    </div>
  );
};

export default UserList;
