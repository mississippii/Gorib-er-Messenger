package veer.chatserver.socket;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import veer.chatserver.streams.StreamDistributor;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final StreamDistributor streamDistributor;
    public final ConcurrentHashMap<String, WebSocketSession> clients = new ConcurrentHashMap<>();
    public final HashMap<String, String> activeUser = new HashMap<>();

    public ChatWebSocketHandler(StreamDistributor streamDistributor) {
        this.streamDistributor = streamDistributor;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String ip = Objects.requireNonNull(session.getRemoteAddress()).getAddress().getHostAddress();
        String port = String.valueOf(session.getRemoteAddress().getPort());
        String clientKey = ip + ":" + port;
        clients.put(clientKey, session);
        activeUser.put(clientKey, session.getId());
    }
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        streamDistributor.distribute(session,message,clients);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String ip = Objects.requireNonNull(session.getRemoteAddress()).getAddress().getHostAddress();
        String port = String.valueOf(session.getRemoteAddress().getPort());
        String clientKey = ip + ":" + port;
        try {
            this.clients.remove(clientKey);
            this.activeUser.remove(clientKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
