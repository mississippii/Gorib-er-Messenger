package veer.chatserver.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import veer.chatserver.entity.User;
import veer.chatserver.service.UserService;
import veer.chatserver.websocket.ChatWebSocketHandler;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(value = "*")
public class SocketController {
    private final UserService userService;
    private final ChatWebSocketHandler chatWebSocketHandler;

    public SocketController(UserService userService, ChatWebSocketHandler chatWebSocketHandler) {
        this.userService = userService;
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @PostMapping("/users")
    public List<User> getUser(){
        return userService.getUsers();
    }
}
