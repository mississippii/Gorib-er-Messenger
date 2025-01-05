// src/components/Sidebar.js
import React from "react";
import "../css/Sidebar.css";

const Sidebar = ({ users, onSelectUser, selectedUser }) => {
  return (
    <div className="sidebar">
      {users.map((user) => (
        <div
          key={user.id}
          className={`user-item ${selectedUser?.id === user.id ? "selected" : ""}`}
          onClick={() => onSelectUser(user)}
        >
          <div className="avatar">
            {user.profilePicture ? (
              <img src={user.profilePicture} alt={user.name} />
            ) : (
              user.name[0] /* Use the first letter as fallback */
            )}
          </div>
          <span className="username">{user.name}</span>
        </div>
      ))}
    </div>
  );
};

export default Sidebar;