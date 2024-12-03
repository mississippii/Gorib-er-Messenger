package veer.chatserver;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import veer.chatserver.Websocket.FlinkConsumer;

@SpringBootApplication
public class Server {

    public static void main(String[] args) throws Exception {
       ApplicationContext context =  SpringApplication.run(Server.class, args);
       FlinkConsumer flinkConsumer = context.getBean(FlinkConsumer.class);
       //flinkConsumer.consume();
    }
}
