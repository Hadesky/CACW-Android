package com.hadesky.cacw.bean;

import com.hadesky.cacw.R;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 *
 * Created by 45517 on 2015/10/17.
 */
public class UserBean extends BmobUser implements Serializable{


    private int avatarResid = R.drawable.default_user_image;
    private String mNickName;


    public String getNickName()
    {
        return mNickName;
    }

    public void setNickName(String nickName)
    {
        mNickName = nickName;
    }

    public UserBean() {
    }

    public UserBean(String nickName)
    {
        mNickName = nickName;
    }

    public int getAvatarResid() {
        return avatarResid;
    }

    public void setAvatarResid(int avatarResid) {
        this.avatarResid = avatarResid;
    }

}
