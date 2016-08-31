package com.hadesky.cacw.presenter;

import com.hadesky.cacw.model.AccountRepertory;
import com.hadesky.cacw.network.BaseResult;
import com.hadesky.cacw.ui.view.LoginView;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 *
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
        mSubscription = mAccountRepertory.login(name, psw)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BaseResult<String>>() {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {
                mView.hideProgress();
            }

            @Override
            public void onNext(BaseResult<String> stringBaseResult)
            {
                mView.hideProgress();
                if (stringBaseResult.getState_code() == 0)
                    mView.onLoginSucceed();
                else
                    mView.showMsg(stringBaseResult.getError_msg());
            }
        });
    }
}
