package com.hadesky.cacw.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.hadesky.cacw.config.MyApp;

import java.io.Serializable;

/**
 *
 * Created by 45517 on 2015/10/17.
 */
public class UserBean implements Parcelable,Serializable
{

    private static final long serialVersionUID = 1L;

    public static final Byte SEX_MALE = 0;//性别男
    public static final Byte SEX_FEMALE = 1;//性别女
    public static final Byte SEX_UNKNOW = 2;//性别保密

    private String username;
    private String nickName;
    private int id;

    private int sex = 2;//0是男，1是女，2是保密
    private String mobilePhone;
    private String shortNumber;//短号
    private String summary;//个人简介
    private String address;//地址
    private String email;
    private String avatarUrl;

    public UserBean()
    {

    }

    public UserBean(String nickName)
    {
        this.nickName = nickName;
    }

    protected UserBean(Parcel in)
    {
        username = in.readString();
        nickName = in.readString();
        id = in.readInt();
        mobilePhone = in.readString();
        shortNumber = in.readString();
        summary = in.readString();
        address = in.readString();
        email = in.readString();
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


    public int getSex() {
        return sex;
    }

    public void setSex(int mSex) {
        this.sex = mSex;
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

    public void setAvatarUrl(String s)
    {
        avatarUrl = s;
    }

    public String getAvatarUrl()
    {
        if(avatarUrl==null||avatarUrl.length()==0)
            return null;
         return   MyApp.getURL()+"/v1/images/user_"+id+"_"+avatarUrl+".jpg";
    }

    @Override
    public String toString() {
        return getNickName();
    }


    @Override
    public boolean equals(Object obj)
    {
        if(obj==null||!(obj instanceof UserBean))
            return false;

        return ((UserBean) obj).getId()==this.id;
    }


    public static final Parcelable.Creator<UserBean> CREATOR = new Creator<UserBean>()
    {
        @Override
        public UserBean createFromParcel(Parcel parcel)
        {
            UserBean u = new UserBean();
            u.setUsername(parcel.readString());
            u.setNickName(parcel.readString());
            u.setId(parcel.readInt());
            u.setMobilePhone(parcel.readString());
            u.setShortNumber(parcel.readString());
            u.setSummary(parcel.readString());
            u.setAddress(parcel.readString());
            u.setEmail(parcel.readString());
            u.setSex(parcel.readInt());
            u.setAvatarUrl(parcel.readString());
            return u;
        }

        @Override
        public UserBean[] newArray(int i)
        {
            return new UserBean[i];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(username);
        parcel.writeString(nickName);
        parcel.writeInt(id);
        parcel.writeString(mobilePhone);
        parcel.writeString(shortNumber);
        parcel.writeString(summary);
        parcel.writeString(address);
        parcel.writeString(email);
        parcel.writeInt(sex);
        parcel.writeString(avatarUrl);
    }
}
