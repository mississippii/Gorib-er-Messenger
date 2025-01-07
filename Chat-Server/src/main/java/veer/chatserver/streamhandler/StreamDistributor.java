package veer.chatserver.streamhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StreamDistributor {

    private final TaskExecutor taskExecutor;
    private final StreamController streamController;

    public StreamDistributor(@Qualifier("webSocketTaskExecutor") TaskExecutor taskExecutor,@Lazy StreamController streamController) {
        this.taskExecutor = taskExecutor;
        this.streamController = streamController;
    }
    public void distribute(WebSocketSession session, TextMessage message, ConcurrentHashMap<String, WebSocketSession> clients){
        taskExecutor.execute(() -> {
            try {
                String messageText = message.getPayload();
                JsonNode rootNode = new ObjectMapper().readTree(messageText);
                String type = rootNode.get("type").asText();

                switch (type) {
                    case "message":
                        streamController.handleTextMessage(session, messageText, clients);
                        break;
                    case "api":
                        streamController.handleApiRequest(session, messageText);
                        break;
                    case "audio":
                        streamController.handleAudioStream(session, messageText);
                        break;
                    case "video":
                        streamController.handleVideoStream(session, messageText);
                        break;
                    case "registration":
                        streamController.handleRegistration(session,messageText);
                        break;
                    default:
                        session.sendMessage(new TextMessage("Error: Unknown stream type."));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
