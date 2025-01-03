package veer.chatserver.websocket;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import veer.chatserver.streamhandler.StreamDistributor;

import java.util.HashMap;
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
        String ip = session.getRemoteAddress().getAddress().getHostAddress();
        String port = String.valueOf(session.getRemoteAddress().getPort());
        String clientKey = ip + ":" + port;

        clients.put(clientKey, session);
        activeUser.put(clientKey, session.getId());
        System.out.println(clientKey + " connected");
    }
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        streamDistributor.distribute(session,message,clients);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String ip = session.getRemoteAddress().getAddress().getHostAddress();
        String port = String.valueOf(session.getRemoteAddress().getPort());
        String clientKey = ip + ":" + port;
        try {
            this.clients.remove(clientKey);
            this.activeUser.remove(clientKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Client disconnected: " + clientKey);
    }
}
