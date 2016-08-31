package com.hadesky.cacw.presenter;

import com.hadesky.cacw.model.AccountRepertory;
import com.hadesky.cacw.network.BaseResult;
import com.hadesky.cacw.ui.view.RegisterView;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/** 注册
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
        mSubscription = mAccountRepertory.register(email,pw)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResult<String>>() {
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
                if(stringBaseResult.getState_code()==6)
                    mView.showMsg("用户名已经存在");
                else if(stringBaseResult.getState_code()==0)
                {
                    mView.showMsg("注册成功");
                    mView.close();
                }
                else{
                    mView.showMsg(stringBaseResult.getError_msg());
                }
            }
        });
    }

    @Override
    public void cancelRequest()
    {
        if(mSubscription!=null)
            mSubscription.unsubscribe();
    }
}
