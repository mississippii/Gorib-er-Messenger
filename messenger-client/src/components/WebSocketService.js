let socketInstance = null;

export const createWebSocket = (url) => {
  if (!socketInstance || socketInstance.readyState === WebSocket.CLOSED) {
    console.log("Creating new WebSocket connection...");
    socketInstance = new WebSocket(url);

    socketInstance.onopen = () => {
      console.log("WebSocket connection established.");
    };

    socketInstance.onclose = () => {
      console.log("WebSocket connection closed.");
      socketInstance = null; // Reset instance
    };

    socketInstance.onerror = (error) => {
      console.error("WebSocket error:", error);
    };
  } else if (socketInstance.readyState === WebSocket.CONNECTING) {
    console.log("WebSocket is still connecting...");
  }

  return socketInstance;
};

export const closeWebSocket = () => {
  if (socketInstance && socketInstance.readyState !== WebSocket.CLOSED) {
    socketInstance.close();
    socketInstance = null;
  }
};

export const getWebSocketInstance = () => socketInstance;
