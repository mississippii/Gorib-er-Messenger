import React from 'react';
import ChatList from './ChatList';
import SearchBar from './SearchBar';
import SidebarHeader from './SidebarHeader';

const UserList = ({ users, onSelectUser, selectedUser }) => {
  return (
    <div className="w-full max-w-md mx-auto bg-white rounded-lg shadow-md">
      <SidebarHeader />
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
