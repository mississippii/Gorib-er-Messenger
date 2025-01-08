package veer.chatserver.dto;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
public class ConcurrentUserDto implements Serializable {
    String type;
    HashMap<String,String> userList;
    public ConcurrentUserDto() {
    }

    public ConcurrentUserDto(String type, HashMap<String, String> userList) {
        this.type = type;
        this.userList = userList;
    }
}
