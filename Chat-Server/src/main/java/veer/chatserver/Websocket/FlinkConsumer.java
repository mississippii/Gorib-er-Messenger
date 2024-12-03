package veer.chatserver.Websocket;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class FlinkConsumer {

    public void consume() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties kafkaProperties = new Properties();
        kafkaProperties.setProperty("bootstrap.servers", "1033.248.1.73:9092");
        kafkaProperties.setProperty("group.id", "chat_group");

        DataStream<String> messages = env
                .addSource(new FlinkKafkaConsumer<>("flink1", new SimpleStringSchema(), kafkaProperties));

        messages.map(message -> {
            System.out.println("Processing message: " + message);
            return message;
        }).addSink(new FlinkKafkaProducer<>(
                "103.248.1.73:9092",
                "processed_chat_topic",
                new SimpleStringSchema()));

        env.execute("Chat Application Flink Processor");
    }
}
