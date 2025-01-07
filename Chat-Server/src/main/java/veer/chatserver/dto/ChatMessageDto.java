package veer.chatserver.dto;

import lombok.Data;

@Data
public class ChatMessageDto extends BaseMessage{
    private String sender;
    private String receiver;
    private String text;
}
