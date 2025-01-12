import React from 'react';
import ChatList from './ChatList';
import SearchBar from './SearchBar';
import SideHeader from './SideHeader';

const UserList = ({ users, onSelectUser, selectedUser }) => {
  return (
    <div className="w-full h-full bg-white shadow-right flex flex-col p-1 py-4 gap-3 items-center">
      {/* Header section */}
      <SideHeader />

      {/* Search bar */}
      <SearchBar />

      {/* Chat list section */}
      <ChatList
        users={users} // Pass the list of users with ip and name
        onSelectUser={onSelectUser} // Handle user selection
        selectedUser={selectedUser} // Pass currently selected user's IP
      />
    </div>
  );
};

export default UserList;
