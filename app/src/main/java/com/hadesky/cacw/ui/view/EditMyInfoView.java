package com.hadesky.cacw.ui.view;

import android.graphics.Bitmap;

import com.hadesky.cacw.bean.UserBean;

/**
 * Created by MicroStudent on 2016/7/10.
 */

public interface EditMyInfoView extends BaseView {
    void setAvatar(String avatarUrl);

    void setSex(Byte sex);

    void setSummary(String summary);

    void setNickName(String nickName);

    void setUserName(String userName);

    void setPhoneNumber(String phoneNumber);

    void setShortPhoneNumber(String shortNumber);

    void setAddress(String address);
}
