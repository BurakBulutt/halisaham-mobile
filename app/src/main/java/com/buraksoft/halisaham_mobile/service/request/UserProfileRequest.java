package com.buraksoft.halisaham_mobile.service.request;

public class UserProfileRequest {
    private String userId;
    private byte[] photo;

    public UserProfileRequest() {
    }

    public UserProfileRequest(String userId, byte[] photo) {
        this.userId = userId;
        this.photo = photo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
