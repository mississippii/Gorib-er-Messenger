package veer.webclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebClientApp implements CommandLineRunner {

    @Autowired
    private WebSocketChatClient chatClient;

    public static void main(String[] args) {
        SpringApplication.run(WebClientApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        chatClient.connect("ws://localhost:8080/chat");
        chatClient.sendMessage("Hello Server!");
    }
}