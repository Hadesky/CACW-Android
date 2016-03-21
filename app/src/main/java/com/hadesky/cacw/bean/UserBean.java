package com.hadesky.cacw.bean;

import com.hadesky.cacw.R;

import java.io.Serializable;

/**
 *
 * Created by 45517 on 2015/10/17.
 */
public class UserBean implements Serializable{

    private String username;

    private long user_id;

    private int avatarResid = R.drawable.default_user_image;


    public UserBean() {
    }

    public UserBean(String username, int userId) {
        this.username = username;
        this.user_id = userId;
    }


    public String getUsername() {
        return username;
    }

    public int getAvatarResid() {
        return avatarResid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarResid(int avatarResid) {
        this.avatarResid = avatarResid;
    }

    public long getUserId() {
        return user_id;
    }

    public void setUserId(long user_id) {
        this.user_id = user_id;
    }
}
