package com.project.bilal.soundchat.model;

/**
 * Created by Bilal Abdullah on 2/11/2017.
 */

public class Message {
    private String message;
    private MessageType type;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }
}
