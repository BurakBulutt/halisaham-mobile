package com.buraksoft.halisaham_mobile.model;

import android.util.Base64;

public class AreaModel {
    private String id;
    private String name;
    private String photo;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPhoto() {
        return photo != null ? Base64.decode(photo,Base64.DEFAULT) : null;
    }

    public void setPhoto(byte[] photo) {
        this.photo = Base64.encodeToString(photo,Base64.DEFAULT);
    }
}
