package com.hadesky.cacw.bean;

import java.io.Serializable;

/**
 *  消息实体
 * Created by MicroStudent on 2016/7/16.
 */

public class MessageBean implements Serializable
{

    public static final int TYPE_USER_TO_TEAM = 1;
    public static final int TYPE_TEAM_TO_USER = 0;
    public static final int TYPE_USER_TO_USER = 2;
    private int id;
    private UserBean sender;
    private UserBean mReceiver;
    private UserBean other;//这个永远指别人

    private int type;
    private String content;
    private boolean hasRead = false;
    private int teamid;
    private boolean isMe = false;//这个消息是否是自己发的


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


    public UserBean getOther()
    {
        return other;
    }

    public void setOther(UserBean other)
    {
        this.other = other;
    }

    public boolean isMe()
    {
        return isMe;
    }

    public void setMe(boolean me)
    {
        isMe = me;
    }

    public void setHasRead(boolean hasRead)
    {
        this.hasRead = hasRead;
    }

    public int getTeamid()
    {
        return teamid;
    }

    public void setTeamid(int teamid)
    {
        this.teamid = teamid;
    }

    public Boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(Boolean hasRead) {
        this.hasRead = hasRead;
    }

    public UserBean getSender() {
        return sender;
    }

    public void setSender(UserBean sender) {
        this.sender = sender;
    }

    public UserBean getReceiver() {
        return mReceiver;
    }

    public void setReceiver(UserBean receiver) {
        mReceiver = receiver;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
