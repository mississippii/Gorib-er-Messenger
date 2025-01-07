package veer.chatserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import veer.chatserver.dto.UserDto;
import veer.chatserver.entity.User;
import veer.chatserver.service.UserService;
import veer.chatserver.websocket.ChatWebSocketHandler;

import java.util.List;

@RestController
@CrossOrigin(value = "*")
public class SocketController {
    private final UserService userService;

    @Autowired
    public SocketController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public List<UserDto> getUser(){
        return userService.getAllUser();
    }
}
