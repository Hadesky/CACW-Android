package com.hadesky.cacw.bean;


import java.io.Serializable;

/**
 *
 * Created by 45517 on 2015/10/17.
 */
public class UserBean implements Serializable
{

    public static final Byte SEX_MALE = 0;//性别男
    public static final Byte SEX_FEMALE = 1;//性别女
    public static final Byte SEX_UNKNOW = 2;//性别保密

    private String username;
    private String nickName;
    private int id;
    private Byte mSex = 0;//0是男，1是女，2是保密
    private String mobilePhone;
    private String shortNumber;//短号
    private String summary;//个人简介
    private String address;//地址
    private String email;

    public UserBean()
    {

    }

    public UserBean(String nickName)
    {
        this.nickName = nickName;
    }
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNickName()
    {
        if (nickName == null) {
            return "蚂蚁";
        }
        return nickName;
    }


    public int  getId()
    {
        return id;
    }

    public void setId(int  id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }


    public Byte getSex() {
        return mSex;
    }

    public void setSex(Byte mSex) {
        this.mSex = mSex;
    }

    public String getShortNumber() {
        return shortNumber;
    }

    public void setShortNumber(String mShortNumber) {
        this.shortNumber = mShortNumber;
    }

    public String getMobilePhone()
    {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone)
    {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAvatarUrl()
    {
       return "";
    }

    @Override
    public String toString() {
        return getNickName();
    }


}
