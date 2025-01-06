package veer.chatserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    String name;
    String ipPort;
}
