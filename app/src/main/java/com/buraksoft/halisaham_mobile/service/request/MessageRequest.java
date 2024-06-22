package com.buraksoft.halisaham_mobile.service.request;

public class MessageRequest {
    private String message;
    private String user;
    private String chatId;

    public MessageRequest(String message, String user, String chatId) {
        this.message = message;
        this.user = user;
        this.chatId = chatId;
    }

    public MessageRequest() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
