package veer.chatserver.Websocket;

import lombok.Data;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final Producer<String, String> producer;
    private final ConcurrentHashMap<String, WebSocketSession> clients = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(Producer<String, String> producer) {
        this.producer = producer;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String clientId = session.getRemoteAddress().toString().split("/")[1];
        clients.put(clientId, session);
    }
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String senderId = session.getRemoteAddress().toString().split("/")[1];
        producer.send(new ProducerRecord<>("flink1", senderId+": "+payload));
        System.out.println("Message from "+senderId+": " + payload);

        String recipientId = clients.keySet().stream()
                .filter(id -> !id.equals(senderId))
                .findFirst()
                .orElse(null);

        if (recipientId != null) {
            WebSocketSession recipientSession = clients.get(recipientId);
            if (recipientSession != null && recipientSession.isOpen()) {
                recipientSession.sendMessage(new TextMessage(senderId + ": " + payload));
            }
        } else {
            session.sendMessage(new TextMessage("Error: No other client connected."));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String clientId = session.getRemoteAddress().toString().split("/")[1];
        clients.remove(clientId);
        System.out.println("Client disconnected: " + clientId);
    }
}
