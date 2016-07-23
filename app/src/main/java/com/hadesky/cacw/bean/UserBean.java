package com.hadesky.cacw.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 *
 * Created by 45517 on 2015/10/17.
 */
public class UserBean extends BmobUser{

    public static final Byte SEX_MALE = 0;//性别男
    public static final Byte SEX_FEMALE = 1;//性别女
    public static final Byte SEX_UNKNOW = 2;//性别保密

    private String mAvatarUrl;

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
        if (mNickName == null) {
            return "蚂蚁";
        }
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

    public String getAvatarUrl()
    {
        if (mAvatarUrl==null&&mUserAvatar!=null)
        {
            mAvatarUrl = mUserAvatar.getUrl();
        }
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl)
    {
        mAvatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return getNickName();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof UserBean) {
            if (((UserBean) o).getObjectId().equals(getObjectId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        if (getObjectId() != null) {
            result = 31 * result + getObjectId().hashCode();
        }
        return result;
    }
}
