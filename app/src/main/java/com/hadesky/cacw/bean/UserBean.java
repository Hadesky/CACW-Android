package com.hadesky.cacw.bean;

import com.hadesky.cacw.R;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 *
 * Created by 45517 on 2015/10/17.
 */
public class UserBean extends BmobUser implements Serializable{

    public static final Byte SEX_MALE = 0;//性别男
    public static final Byte SEX_FEMALE = 1;//性别女
    public static final Byte SEX_UNKNOW = 2;//性别保密

    private BmobFile mUserAvatar;

    private String mNickName;

    private Byte mSex = 0;//0是男，1是女，2是保密

    private String mShortNumber;//短号

    private String mSummary;//个人简介

    private String mAddress;//地址

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getNickName()
    {
        return mNickName;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
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

    public BmobFile getUserAvatar() {
        return mUserAvatar;
    }

    public void setUserAvatar(BmobFile mUserAvatar) {
        this.mUserAvatar = mUserAvatar;
    }


    public Byte getSex() {
        return mSex;
    }

    public void setSex(Byte mSex) {
        this.mSex = mSex;
    }

    public String getShortNumber() {
        return mShortNumber;
    }

    public void setShortNumber(String mShortNumber) {
        this.mShortNumber = mShortNumber;
    }
}
