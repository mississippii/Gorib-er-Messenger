package veer.chatserver.streamhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public StreamDistributor(@Qualifier("webSocketTaskExecutor") TaskExecutor taskExecutor, StreamController streamController) {
        this.taskExecutor = taskExecutor;
        this.streamController = streamController;
    }
    public void distribute(WebSocketSession session, TextMessage message, ConcurrentHashMap<String, WebSocketSession> clients){
        taskExecutor.execute(() -> {
            try {
                String messageText = message.getPayload();
                StreamDto msg = parseMessage(messageText);

                switch (msg.getType()) {
                    case "message":
                        streamController.handleTextMessage(session, msg, clients);
                        break;
                    case "api":
                        streamController.handleApiRequest(session, msg);
                        break;
                    case "audio":
                        streamController.handleAudioStream(session, msg);
                        break;
                    case "video":
                        streamController.handleVideoStream(session, msg);
                        break;
                    default:
                        session.sendMessage(new TextMessage("Error: Unknown stream type."));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private StreamDto parseMessage(String messageText) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(messageText, StreamDto.class);
    }
}
