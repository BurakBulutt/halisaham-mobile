package com.buraksoft.halisaham_mobile.model;

import android.util.Base64;

public class UserProfileModel {
    private String id;
    private UserModel user;
    private String photo;

    public UserProfileModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel getUserModel() {
        return user;
    }

    public void setUserModel(UserModel user) {
        this.user = user;
    }

    public byte[] getPhoto() {
        return photo != null ? Base64.decode(photo, Base64.DEFAULT) : null;
    }

    public void setPhoto(byte[] photo) {
        this.photo = Base64.encodeToString(photo, Base64.DEFAULT);
    }
}
