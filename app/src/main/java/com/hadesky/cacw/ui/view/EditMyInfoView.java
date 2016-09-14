package com.hadesky.cacw.ui.view;

/**
 * Created by MicroStudent on 2016/7/10.
 */

public interface EditMyInfoView extends BaseView {
    void setAvatar(String avatarUrl);

    void setSex(int sex);

    void setSummary(String summary);

    void setNickName(String nickName);

    void setUserName(String userName);

    void setPhoneNumber(String phoneNumber);

    void setShortPhoneNumber(String shortNumber);

    void setAddress(String address);
}
