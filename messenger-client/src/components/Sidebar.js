// src/components/Sidebar.js
import React from "react";
import "../css/Sidebar.css";

const Sidebar = ({ users, onSelectUser, selectedUser }) => {
  return (
    <div className="sidebar">
      {users?.map((user) => (
        <div
          key={user}
          className={`user-item ${selectedUser === user ? "selected" : ""}`}
          onClick={() => onSelectUser(user)}
        >
          <span className="username">{user}</span>
        </div>
      ))}
    </div>
  );
};

export default Sidebar;