package veer.chatserver.service;

import org.springframework.stereotype.Service;
import veer.chatserver.dto.UserDto;
import veer.chatserver.entity.User;
import veer.chatserver.repository.UserRepo;
import veer.chatserver.streamhandler.StreamDistributor;
import veer.chatserver.websocket.ChatWebSocketHandler;

import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final ChatWebSocketHandler chatWebSocketHandler;

    public UserService(UserRepo userRepo, StreamDistributor streamDistributor, ChatWebSocketHandler chatWebSocketHandler) {
        this.userRepo = userRepo;
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    public List<UserDto> getAllUser() {
        return null;
    }
    public void saveOrUpdateUser(UserDto userDto) {
        String name = userDto.getName();
        String password = userDto.getPassword();
        String ipPort = userDto.getIpPort();
        User existingUser = userRepo.findByName(name);
        if (existingUser != null) {
            if (existingUser.getPassword().equals(password)) {
                existingUser.setIpPort(ipPort);
                userRepo.save(existingUser);
            } else {
                throw new RuntimeException("Password mismatch for user: " + name);
            }
        } else {
            User newUser = new User();
            newUser.setName(name);
            newUser.setPassword(password);
            newUser.setIpPort(ipPort);
            if(newUser.getPassword()=="") {
                newUser.setPassword(name);
            }
            userRepo.save(newUser);
        }
        HashMap<String,String> map = chatWebSocketHandler.activeUser;
        map.put(ipPort,name);
    }
}
