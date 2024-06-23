package com.buraksoft.halisaham_mobile.model;

import java.util.Date;

public class MessageModel {
    private UserModel user;
    private String chatId;
    private String message;
    private Date messageDate;

    public MessageModel() {

    }

    public MessageModel(UserModel user, String chatId, String message,Date messageDate) {
        this.user = user;
        this.chatId = chatId;
        this.message = message;
        this.messageDate = messageDate;
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

    public Date getMessageDate(){
        return messageDate;
    }
}
