package veer.chatserver.dto;

import java.io.Serializable;
import java.util.HashMap;

public class ConcurrentUserDto implements Serializable {
    String type;
    HashMap<String,String> userList;
    public ConcurrentUserDto() {
    }

    public String getType() {
        return type;
    }

    public HashMap<String, String> getUserList() {
        return userList;
    }

    public ConcurrentUserDto(String type, HashMap<String, String> userList) {
        this.type = type;
        this.userList = userList;
    }
}
