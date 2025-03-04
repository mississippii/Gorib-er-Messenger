package veer.chatserver.socket;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
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
    private final TaskExecutor executor;
    public final ConcurrentHashMap<String, WebSocketSession> clients = new ConcurrentHashMap<>();
    public final HashMap<String, String> activeUser = new HashMap<>();

    public ChatWebSocketHandler(StreamDistributor streamDistributor, @Qualifier("webSocketTaskExecutor") TaskExecutor executor) {
        this.streamDistributor = streamDistributor;
        this.executor = executor;
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
    public void handleTextMessage(WebSocketSession session, TextMessage message){
        executor.execute(() -> {
            try {
                streamDistributor.distribute(session, message, clients);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
