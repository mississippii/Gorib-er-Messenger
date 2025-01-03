package veer.chatserver.streamhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import veer.chatserver.websocket.ChatWebSocketHandler;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ActiveUserBroadcaster {

    private final ChatWebSocketHandler chatWebSocketHandler;

    public ActiveUserBroadcaster(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Scheduled(fixedRate = 200000)
    public void broadcastActiveUsers() {
        try {
            UserDto activeUsers = new UserDto("users",chatWebSocketHandler.activeUser);
            ObjectMapper objectMapper = new ObjectMapper();
            String activeUsersJson = objectMapper.writeValueAsString(activeUsers);
            ConcurrentHashMap<String, WebSocketSession> clients= chatWebSocketHandler.clients;
            for (WebSocketSession session : clients.values()) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(activeUsersJson));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

