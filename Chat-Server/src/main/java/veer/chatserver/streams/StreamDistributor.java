package veer.chatserver.streams;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class StreamDistributor {

    private final StreamController streamController;

    public StreamDistributor(StreamController streamController) {
        this.streamController = streamController;
    }

    public void distribute(WebSocketSession session, TextMessage message, ConcurrentHashMap<String, WebSocketSession> clients) {
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
                    streamController.handleRegistration(session, messageText);
                    break;
                default:
                    session.sendMessage(new TextMessage("Error: Unknown stream type."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
