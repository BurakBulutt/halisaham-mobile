package com.buraksoft.halisaham_mobile.model;

import java.util.Date;

public class MessageModel {
    private UserModel user;
    private String chatId;
    private String message;

    public MessageModel() {

    }

    public MessageModel(UserModel user, String chatId, String message) {
        this.user = user;
        this.chatId = chatId;
        this.message = message;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
