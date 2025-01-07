import React, { useState } from 'react';
import '../css/LoginPage.css';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSignIn = () => {
    // Handle sign-in logic here
    console.log('Signing in with:', username, password);
  };

  const handleSignUp = () => {
    // Handle sign-up logic here
    console.log('Signing up with:', username, password);
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <h2>Login</h2>
        <form>
          <div className="input-container">
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="input-field"
            />
          </div>
          <div className="input-container">
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="input-field"
            />
          </div>
          <div className="button-container">
            <button type="button" onClick={handleSignIn} className="button sign-in-button">Login</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;
