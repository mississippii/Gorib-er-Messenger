package veer.chatserver.streamhandler;

import java.io.Serializable;
import java.util.HashMap;

public class UserDto implements Serializable {
    String type;
    HashMap<String,String> userList;
    public UserDto() {
    }

    public String getType() {
        return type;
    }

    public HashMap<String, String> getUserList() {
        return userList;
    }

    public UserDto(String type, HashMap<String, String> userList) {
        this.type = type;
        this.userList = userList;
    }
}
