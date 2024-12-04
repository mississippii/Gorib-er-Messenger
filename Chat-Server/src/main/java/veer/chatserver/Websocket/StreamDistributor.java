package veer.chatserver.Websocket;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class StreamDistributor {

    private final TaskExecutor taskExecutor;
    private final Producer<String, String> producer;

    public StreamDistributor(@Qualifier("webSocketTaskExecutor") TaskExecutor taskExecutor, Producer<String, String> producer) {
        this.taskExecutor = taskExecutor;
        this.producer = producer;
    }
    public void distribute(WebSocketSession session, TextMessage message, ConcurrentHashMap<String, WebSocketSession> clients){
        taskExecutor.execute(() -> {
            try {
                String payload = message.getPayload();
                String senderId = session.getRemoteAddress().toString().split("/")[1];

                String[] parts = payload.split("->", 2);
                String recipientId = parts[0].trim();
                String messageContent = parts[1].trim();

                producer.send(new ProducerRecord<>("flink1", senderId+": "+payload));

                WebSocketSession recipientSession = clients.get(recipientId);
                if (recipientSession != null && recipientSession.isOpen()) {
                    recipientSession.sendMessage(new TextMessage(senderId + ": " + messageContent));
                } else {
                    session.sendMessage(new TextMessage("Error: Recipient not connected or session closed."));
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
    }
}
