package com.buraksoft.halisaham_mobile.model;

import java.util.List;

public class ChatModel {
    private String id;
    private String eventId;

    public ChatModel() {
    }

    public ChatModel(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getId(){
        return id;
    }

}
