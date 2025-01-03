package veer.chatserver.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import veer.chatserver.websocket.ChatWebSocketHandler;

import java.util.HashMap;

@RestController
@CrossOrigin(value = "*")
public class SocketController {
    private final ChatWebSocketHandler chatWebSocketHandler;

    public SocketController(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }
}
