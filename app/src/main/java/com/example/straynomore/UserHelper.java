package com.example.straynomore;

import com.google.firebase.firestore.auth.User;

public class UserHelper {
    String name, email, password, userType, img;

    public UserHelper(){}

    public UserHelper(String name, String email, String password, String userType, String img) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
