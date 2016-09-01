package com.hadesky.cacw.presenter;

import com.hadesky.cacw.model.AccountRepertory;
import com.hadesky.cacw.ui.view.LoginView;

import rx.Subscriber;
import rx.Subscription;

/** 登录
 * Created by dzysg on 2016/8/31 0031.
 */
public class LoginPresenterImpl implements LoginPresenter
{

    LoginView mView;
    AccountRepertory mAccountRepertory;
    Subscription mSubscription;

    public LoginPresenterImpl(LoginView view)
    {
        mView = view;
        mAccountRepertory = new AccountRepertory();
    }

    @Override
    public void login(String name, String psw)
    {
        mView.showProgress();
        mSubscription = mAccountRepertory.login(name, psw).subscribe(new Subscriber<String>()
        {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {
                mView.hideProgress();
                mView.showMsg(e.getMessage());
            }

            @Override
            public void onNext(String stringBaseResult)
            {
                mView.hideProgress();
                mView.onLoginSucceed();

            }
        });
    }
}
