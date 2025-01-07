package veer.chatserver.dto;


import lombok.Data;


@Data
public class UserDto extends BaseMessage  {
    String name;
    String password;
    String ipPort;
}
