package veer.chatserver.config;

import lombok.Data;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import veer.chatserver.Websocket.FlinkConsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Data
@Configuration
public class KafkaProducerConfig {

    @Bean
    public Properties producerProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "103.248.13.73:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return props;
    }
    @Bean
    public Producer<String, String> kafkaProducer() {
        return new KafkaProducer<>(producerProperties());
    }
}