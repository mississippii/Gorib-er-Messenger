package veer.chatserver.streamhandler;

import java.io.Serializable;

public class StreamDto implements Serializable {
    private String type;
    private String sender;
    private String receiver;
    private String text;
    public StreamDto() {
    }
    public StreamDto(String type, String sender, String receiver, String text) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }

    public String getType() {
        return type;
    }
    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setText(String text) {
        this.text = text;
    }
}
