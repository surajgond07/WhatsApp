package com.surajinfotech.whatsapp.Models;

public class MessageModel {

    String uId, message;
    Long timestamp;

    // Creating Constructor

    public MessageModel(String uId, String message, Long timestamp) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Creating Constructor for only msg & Id
    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }


    // Creating Empty Constructor
    public MessageModel(){}

    // Creating Setter & Getter
    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
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
