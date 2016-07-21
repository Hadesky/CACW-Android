package com.hadesky.cacw.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by MicroStudent on 2016/7/16.
 */

public class MessageBean extends BmobObject {

    public static final Byte TYPE_USER_TO_TEAM = 1;
    public static final Byte TYPE_TEAM_TO_USER = 0;
    public static final Byte TYPE_USER_TO_USER = 2;
    private UserBean mSender;
    private UserBean mReceiver;
    private Byte mType;
    private String mMsg;
    private Boolean hasRead = false;

    public Boolean getHasRead() {
        return hasRead;
    }

    public void setHasRead(Boolean hasRead) {
        this.hasRead = hasRead;
    }

    public UserBean getSender() {
        return mSender;
    }

    public void setSender(UserBean sender) {
        mSender = sender;
    }

    public UserBean getReceiver() {
        return mReceiver;
    }

    public void setReceiver(UserBean receiver) {
        mReceiver = receiver;
    }

    public Byte getType() {
        return mType;
    }

    public void setType(Byte type) {
        mType = type;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }
}
