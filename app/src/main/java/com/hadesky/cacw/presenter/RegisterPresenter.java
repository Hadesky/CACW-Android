package com.hadesky.cacw.presenter;

/**
 * Created by 45517 on 2015/11/22.
 */
public interface RegisterPresenter {
    void getAuthCode(String email);

    void register(String email, String pw, String authCode);

    void cancelRequest();
}
