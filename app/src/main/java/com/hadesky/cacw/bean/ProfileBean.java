package com.hadesky.cacw.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * 一些基本的用户资料
 * Created by 45517 on 2015/9/18.
 */
public class ProfileBean {
    private String userName;
    private String userEmail;
    private Bitmap userAvatar;//头像
    private String userPhoneNumber;

    public ProfileBean() {
    }

    public ProfileBean(String userName, boolean isLogin, String userEmail, Bitmap userAvatar, String userPhoneNumber) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userAvatar = userAvatar;
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserName() {
        return userName;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public Bitmap getUserAvatar() {
        return userAvatar;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserAvatar(Bitmap userAvatar) {
        this.userAvatar = userAvatar;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
}
