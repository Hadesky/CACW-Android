package com.hadesky.cacw.presenter;

import com.hadesky.cacw.model.AccountRepertory;
import com.hadesky.cacw.ui.view.RegisterView;

import rx.Subscriber;
import rx.Subscription;

/**
 * 注册
 * Created by dzysg on 2016/8/31 0031.
 */
public class RegisterPresenterImpl implements RegisterPresenter
{

    private AccountRepertory mAccountRepertory;
    private RegisterView mView;
    private Subscription mSubscription;

    public RegisterPresenterImpl(RegisterView view)
    {
        mView = view;
        mAccountRepertory = new AccountRepertory();
    }

    @Override
    public void getAuthCode(String email)
    {

    }

    @Override
    public void register(String email, String pw, String authCode)
    {
        mView.showProgress();
        mSubscription = mAccountRepertory.register(email, pw).subscribe(new Subscriber<String>()
        {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {
                mView.showMsg(e.getMessage());
                mView.hideProgress();
            }

            @Override
            public void onNext(String Result)
            {
                mView.hideProgress();
                mView.showMsg("注册成功");
                mView.close();
            }
        });
    }


    @Override
    public void cancelRequest()
    {
        if(mSubscription!=null&&!mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }
}
