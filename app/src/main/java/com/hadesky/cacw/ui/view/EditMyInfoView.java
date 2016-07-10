package com.hadesky.cacw.ui.view;

import android.graphics.Bitmap;

import com.hadesky.cacw.bean.UserBean;

/**
 * Created by MicroStudent on 2016/7/10.
 */

public interface EditMyInfoView extends BaseView {
    void setAvatar(Bitmap avatar);

    void setSex(Byte sex);

    void setSummary(String summary);

    void setNickName(String nickName);

    void setUserName(String userName);
}
