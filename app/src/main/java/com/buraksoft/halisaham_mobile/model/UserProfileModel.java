package com.buraksoft.halisaham_mobile.model;

public class UserProfileModel {
    private String id;
    private UserModel userModel;
    private byte[] photo;

    public UserProfileModel() {
    }

    public UserProfileModel(String id, UserModel userModel, byte[] photo) {
        this.id = id;
        this.userModel = userModel;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
