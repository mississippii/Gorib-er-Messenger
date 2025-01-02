package veer.chatserver.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import veer.chatserver.messagehandler.Message;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StreamDistributor {

    private final TaskExecutor taskExecutor;

    public StreamDistributor(@Qualifier("webSocketTaskExecutor") TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
    public void distribute(WebSocketSession session, TextMessage message, ConcurrentHashMap<String, WebSocketSession> clients){
        taskExecutor.execute(() -> {
            try {
                String messageText = message.getPayload();
                Message msg = parseMessage(messageText);
                InetSocketAddress remoteAddress = (InetSocketAddress) session.getRemoteAddress();
                String sender = remoteAddress.getAddress().getHostAddress() + ":" + remoteAddress.getPort();
                String receiver = msg.getReceiver();

                WebSocketSession recipientSession = clients.get(receiver);
                if (recipientSession != null && recipientSession.isOpen()) {
                    String formattedMessage = "{\"sender\": \"" + sender + "\", \"receiver\": \"" + receiver + "\", \"text\": \"" + msg.getText() + "\"}";
                    recipientSession.sendMessage(new TextMessage(formattedMessage));
                } else {
                    session.sendMessage(new TextMessage("Error: Recipient not connected or session closed."));
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
    }
    private Message parseMessage(String messageText) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(messageText, Message.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new Message("Error", "Error", "Invalid message format");
        }
    }
}
