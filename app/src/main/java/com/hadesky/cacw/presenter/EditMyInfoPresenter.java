package com.hadesky.cacw.presenter;

import android.net.Uri;

/**
 * Created by MicroStudent on 2016/7/10.
 */

public interface EditMyInfoPresenter {

    void updateAvatar(String avatarPath);

    void updateSexual(Byte sex);

    void updateSummary(String summary);

    void updateNickName(String nickName);

    void loadInfo();

    void updatePhone(String phoneNumber);

    void updateShortPhone(String shortPhone);

    void updateAddress(String address);
}
