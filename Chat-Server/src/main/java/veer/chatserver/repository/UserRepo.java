package veer.chatserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import veer.chatserver.entity.User;

public interface UserRepo extends JpaRepository<User, String> {
    User findByName(String name);
}
