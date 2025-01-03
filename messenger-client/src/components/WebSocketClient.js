import React, { useState, useEffect } from "react";
import axios from "axios";
import { createWebSocket, closeWebSocket, getWebSocketInstance } from "./WebSocketService";

function WebSocketClient() {
  const [message, setMessage] = useState("");
  const [messages, setMessages] = useState([]);
  const [activeUsers, setActiveUsers] = useState([]);
  const [receiver, setReceiver] = useState(null);
  const [senderIp, setSenderIp] = useState("");
  const [senderPort, setSenderPort] = useState("");

  useEffect(() => {
    const socket = createWebSocket("ws://192.168.0.132:8880/chat");
    socket.addEventListener("open", event => {
      socket.send("Connection established")
    });

    socket.addEventListener("message", event => {
      console.log("Message from server ", event.data)
    });

    return () => {
      closeWebSocket();
    };
  }, []);

  const sendMessage = () => {
    const socket = getWebSocketInstance();
    console.log("WebSocket instance:", socket);

    if (socket && socket.readyState === WebSocket.OPEN && message && receiver) {
      const msg = {
        sender: `${senderIp}:${senderPort}`,
        receiver: receiver,
        text: message,
      };

      console.log("Sending message:", msg);
      socket.send(JSON.stringify(msg));
      setMessages((prevMessages) => [...prevMessages, `You: ${message}`]);
      setMessage("");
    } else {
      console.log("Missing data to send message or WebSocket not open");
    }
  };

  return (
    <div>
      <h1>Chat Application</h1>

      <div>
        <h2>Active Users</h2>
        <ul>
          {activeUsers.length > 0 ? (
            activeUsers.map((user) => (
              <li key={user} onClick={() => setReceiver(user)}>
                {user}
              </li>
            ))
          ) : (
            <p>No active users found.</p>
          )}
        </ul>
      </div>

      {receiver && (
        <div>
          <h2>Chat with {receiver}</h2>
          <div>
            {messages.length > 0 ? (
              messages.map((msg, index) => <p key={index}>{msg}</p>)
            ) : (
              <p>No messages yet.</p>
            )}
          </div>
          <input
            type="text"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder="Type a message"
          />
          <button onClick={sendMessage}>Send</button>
        </div>
      )}
    </div>
  );
}

export default WebSocketClient;
