package com.buraksoft.halisaham_mobile.model;

import com.buraksoft.halisaham_mobile.library.enums.UserType;

public class UserModel {
    private String id;
    private String name;
    private String surname;
    private UserType userType;
    private String email;
    private String password;
    private Boolean isVerified;

    public UserModel() {
    }

    public UserModel(String name, String surname, UserType userType, String email, String password, Boolean isVerified) {
        this.name = name;
        this.surname = surname;
        this.userType = userType;
        this.email = email;
        this.password = password;
        this.isVerified = isVerified;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}
