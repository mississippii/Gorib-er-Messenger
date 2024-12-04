package veer.webclient.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;


@Service
public class WebSocketService {

    private final WebSocketClientService webSocketClientService;

    public WebSocketService(WebSocketClientService webSocketClientService) {
        this.webSocketClientService = webSocketClientService;
    }

    @PostConstruct
    public void connect() {
        String uniqueUserId = "user123";
        String uri = "ws://localhost:8880/chat";
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketConnectionManager connectionManager = new WebSocketConnectionManager(client, webSocketClientService, uri, uniqueUserId);
        connectionManager.setAutoStartup(true);
        connectionManager.start();
    }
}
