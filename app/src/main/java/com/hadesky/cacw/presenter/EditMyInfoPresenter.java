package com.hadesky.cacw.presenter;

import android.net.Uri;

/**
 * Created by MicroStudent on 2016/7/10.
 */

public interface EditMyInfoPresenter {

    void updateUserAvatar(Uri avatarPath);

    void updateSexual(Byte sex);

    void updateSummary(String summary);

    void loadInfo();
}
