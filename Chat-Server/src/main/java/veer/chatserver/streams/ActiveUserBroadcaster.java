package veer.chatserver.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import veer.chatserver.dto.ConcurrentUserDto;
import veer.chatserver.socket.ChatWebSocketHandler;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            List<WebSocketSession> sessionsSnapshot = new ArrayList<>(clients.values());

            for (WebSocketSession currentSession : sessionsSnapshot) {
                if (!currentSession.isOpen()) continue;
                InetSocketAddress currentRemoteAddress = (InetSocketAddress) currentSession.getRemoteAddress();

                HashMap<String, String> filteredUserList = new HashMap<>();
                for (WebSocketSession session : sessionsSnapshot) {
                    if (session == currentSession) continue; // Skip self

                    InetSocketAddress remoteAddress = (InetSocketAddress) session.getRemoteAddress();
                    String ipPort = remoteAddress.getAddress().getHostAddress() + ":" + remoteAddress.getPort();
                    filteredUserList.put(ipPort, chatWebSocketHandler.activeUser.get(ipPort));
                }
                ConcurrentUserDto activeUsers = new ConcurrentUserDto("users", filteredUserList);
                String activeUsersJson = objectMapper.writeValueAsString(activeUsers);
                currentSession.sendMessage(new TextMessage(activeUsersJson));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
