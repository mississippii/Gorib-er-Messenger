package veer.chatserver.streamhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class StreamController {
    public void handleTextMessage(WebSocketSession session, StreamDto msg, ConcurrentHashMap<String, WebSocketSession> clients) throws Exception {
        String receiver = msg.getReceiver();
        String senderIp = session.getRemoteAddress().getAddress().getHostAddress()+":"+session.getRemoteAddress().getPort();
        msg.setSender(senderIp);
        WebSocketSession recipientSession = clients.get(receiver);
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(msg)));
        } else {
            session.sendMessage(new TextMessage("Error: Recipient not connected."));
        }
    }

    public void handleApiRequest(WebSocketSession session, StreamDto msg) {
    }

    public void handleAudioStream(WebSocketSession session, StreamDto msg) {
    }

    public void handleVideoStream(WebSocketSession session, StreamDto msg) {
    }

    public void handleRegistration(WebSocketSession session, StreamDto msg) {

    }

}
