// src/components/Messenger.js
import React, {useEffect, useState } from "react";
import Sidebar from "./Sidebar";
import ChatWindow from "./ChatWindow";
import MessageInput from "./MessageInput";
import "../css/Messenger.css"
const Messenger = () => {
  const [messages, setMessages] = useState(null);
  const [socket, setSocket] = useState(null);
  const [activeUsers, setActiveUsers] = useState(null);
  const [selectedUser, setSelectedUser] = useState(null);
  const [loading,setLoading] = useState("first")
  const [flag,setFlag] = useState(true)

  useEffect(() => {
    const ws = new WebSocket('ws://103.248.13.73:8880/chat');
    setSocket(ws);

    ws.onopen = () => {
      console.log('WebSocket connection opened');
      console.log("socket session:", ws);
    };
    

    ws.onmessage = (event) => {
      const incomingMessage = JSON.parse(event.data);
      switch (incomingMessage.type) {
        case 'users':
          if(flag){
            setLoading("second");
            const userList = incomingMessage.userList;
            setActiveUsers(userList);
            setFlag(false);
          }
          else{
            const userList = incomingMessage.userList;
            const updatedUsers = {...activeUsers};
            for(let key in updatedUsers){
              if (!userList.hasOwnProperty(key)) {
                delete updatedUsers[key]; 
              }
            }
            for (let key in userList) {
              if (!updatedUsers.hasOwnProperty(key)) {
                  updatedUsers[key] = userList[key]; 
              }
              }
            console.log(updatedUsers);
            setActiveUsers({...updatedUsers});
          }
          break;
        case 'message':
          setMessages((prevMessages) => [
            ...prevMessages,
            { sender: incomingMessage.sender, text: incomingMessage.text,receiver:incomingMessage.receiver },
          ]);
          break;
        case 'video':
        case 'audio':
          console.log("Don't Receive video/audio data");
          break;
        default:
          console.log('Unknown message type:', incomingMessage.type);
      }
    };



    ws.onclose = () => {
      console.log('WebSocket connection closed');
    };

    ws.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    return () => {
      console.log('Cleaning up WebSocket...');
      ws.close();
    };
  }, []);

  // useEffect(()=>{
  //   if(activeUsers){
  //     const usersObj = activeUsers?.reduce((acc, current) => {
  //       acc[current] = [];
  //       return acc;
  //     }, {});

  //   }
    
  // },[])

  const sendMessage = (text) => {

   const message = {
        type:"message",
        sender:"",
        receiver:selectedUser,
        text
    }

    if (socket) {
      socket.send(JSON.stringify(message));
    }
  };


  const handleSendMessage = (text) => {
     let updateArray = [];
     let updatedObj = {
      ...messages
     }
      Object.keys(messages).forEach(key => {
        if(key === selectedUser){
          updateArray = [...messages[key],{
            sender: "me",
            receiver: key,
            text
          }]
          updatedObj[key] = [...updateArray]
        }
      })
      setMessages({...updatedObj})
      
  };

  console.log(messages);



  return (
    <div className="messenger">
      {/* <Sidebar
        users={activeUsers}
        selectedUser={selectedUser}
        onSelectUser={setSelectedUser}
      /> */}
      <div className="main">
        <ChatWindow messages={messages ? messages[selectedUser] : null} selectedUser={selectedUser} />
        <MessageInput onSendMessage={handleSendMessage} sendMessage={sendMessage} />
      </div>
    </div>
  );
};

export default Messenger;
