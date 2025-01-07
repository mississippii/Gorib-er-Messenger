package veer.chatserver.streamhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import veer.chatserver.dto.BaseMessage;
import veer.chatserver.dto.ChatMessageDto;
import veer.chatserver.dto.UserDto;
import veer.chatserver.service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StreamController {

    private final UserService userService;

    public StreamController(@Lazy UserService userService) {
        this.userService = userService;
    }

    public void handleTextMessage(WebSocketSession session, String message, ConcurrentHashMap<String, WebSocketSession> clients) throws Exception {
        ChatMessageDto messageDto =(ChatMessageDto) parseMessage(message);
        String receiver = messageDto.getReceiver();
        String senderIp = session.getRemoteAddress().getAddress().getHostAddress()+":"+session.getRemoteAddress().getPort();
        messageDto.setSender(senderIp);
        WebSocketSession recipientSession = clients.get(receiver);
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(messageDto)));
        } else {
            session.sendMessage(new TextMessage("Error: Recipient not connected."));
        }
    }

    public void handleApiRequest(WebSocketSession session, String message) {
    }

    public void handleAudioStream(WebSocketSession session, String message) {
    }

    public void handleVideoStream(WebSocketSession session, String message) {
    }

    public void handleRegistration(WebSocketSession session, String message) throws IOException {
        UserDto userDto =(UserDto) parseMessage(message);
        String senderIp = session.getRemoteAddress().getAddress().getHostAddress()+":"+session.getRemoteAddress().getPort();
        userDto.setIpPort(senderIp);
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "registration");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            userService.saveOrUpdateUser(userDto);
            messageMap.put("text", "Approved");
            String jsonMessage = objectMapper.writeValueAsString(messageMap);
            session.sendMessage(new TextMessage(jsonMessage));
        } catch (Exception e) {
            messageMap.put("text", "!Approved");
            String jsonMessage = objectMapper.writeValueAsString(messageMap);
            session.sendMessage(new TextMessage(jsonMessage));
            throw new RuntimeException(e);
        }

    }
    private BaseMessage parseMessage (String messageText){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(messageText);
            String type = rootNode.get("type").asText();

            switch (type) {
                case "message":
                    return objectMapper.readValue(messageText, ChatMessageDto.class);
                case "registration":
                    return objectMapper.readValue(messageText, UserDto.class);
                default:
                    throw new IllegalArgumentException("Unknown message type: " + type);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
