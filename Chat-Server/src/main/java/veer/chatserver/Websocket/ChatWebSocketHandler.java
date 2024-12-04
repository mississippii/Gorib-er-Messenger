package veer.chatserver.Websocket;

import lombok.Data;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final StreamDistributor streamDistributor;
    private final ConcurrentHashMap<String, WebSocketSession> clients = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(StreamDistributor streamDistributor) {
        this.streamDistributor = streamDistributor;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String clientId = session.getRemoteAddress().toString().split("/")[1];
        clients.put(clientId, session);
        System.out.println(clientId + " connected");
    }
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        streamDistributor.distribute(session,message,clients);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String clientId = session.getRemoteAddress().toString().split("/")[1];
        clients.remove(clientId);
        System.out.println("Client disconnected: " + clientId);
    }
}
