package com.hadesky.cacw.presenter;


import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.ui.view.RegisterView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 *  注册页面
 * Created by 45517 on 2015/11/22.
 */
public class RegisterPresenterImpl implements RegisterPresenter {
    public static final String TAG = RegisterPresenterImpl.class.getSimpleName();

    private RegisterView mView;


    public RegisterPresenterImpl(RegisterView view) {
        mView = view;
    }

    @Override
    public void getAuthCode(String email) {
        return;
    }

    @Override
    public void register(String email, String pw, String authCode) {
        UserBean bean = new UserBean();
        //把邮箱当用户名
        bean.setEmail(email);
        bean.setUsername(email);
        bean.setPassword(pw);
        bean.signUp(new SaveListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e)
            {
                mView.hideProgress();
                if (e==null)
                {
                    mView.showMsg("注册成功");
                    mView.close();
                }else
                {
                    mView.showMsg(e.getMessage());
                }
            }

            @Override
            public void onStart()
            {
                mView.showProgress();
            }
        });
    }

    @Override
    public void cancelRequest() {
        //mView.getMyApp().cancelPendingRequest(TAG);
    }
}
