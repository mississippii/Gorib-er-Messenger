package veer.chatserver.service;

import org.springframework.stereotype.Service;
import veer.chatserver.entity.User;
import veer.chatserver.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users = userRepo.findAll();
        return users;
    }
}
