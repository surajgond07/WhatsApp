package com.surajinfotech.whatsapp.Models;

public class MessageModel {

    String userId, message;
    Long timestamp;

    // Creating Constructor

    public MessageModel(String userId, String message, Long timestamp) {
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Creating Constructor for only msg & Id
    public MessageModel(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    // Creating Empty Constructor
    public MessageModel(){}

    // Creating Setter & Getter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
