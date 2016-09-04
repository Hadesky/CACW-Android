package com.hadesky.cacw.presenter;

import java.io.File;

/**
 * Created by MicroStudent on 2016/7/10.
 */

public interface EditMyInfoPresenter {

    void updateAvatar(File avatar);

    void updateSexual(int sex);

    void updateSummary(String summary);

    void updateNickName(String nickName);

    void loadInfo();

    void updatePhone(String phoneNumber);

    void updateShortPhone(String shortPhone);

    void updateAddress(String address);

    void cancel();
}
