package veer.chatserver.streamhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import veer.chatserver.websocket.ChatWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ActiveUserBroadcaster {

    private final ChatWebSocketHandler chatWebSocketHandler;

    public ActiveUserBroadcaster(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Scheduled(fixedRate = 10000)
    public void broadcastActiveUsers() {
        try {
            ConcurrentHashMap<String, WebSocketSession> clients = chatWebSocketHandler.clients;
            ObjectMapper objectMapper = new ObjectMapper();
            for (Map.Entry<String, WebSocketSession> entry : clients.entrySet()) {
                WebSocketSession session = entry.getValue();
                if (session.isOpen()) {
                    String clientIpPort = session.getRemoteAddress().toString().replace("/", "");
                    Map<String, String> filteredUserList = chatWebSocketHandler.activeUser.entrySet().stream()
                            .filter(e -> !e.getKey().equals(clientIpPort))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    UserDto activeUsers = new UserDto("users", (HashMap<String, String>) filteredUserList);
                    String activeUsersJson = objectMapper.writeValueAsString(activeUsers);
                    session.sendMessage(new TextMessage(activeUsersJson));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
