package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.UserRepertory;
import com.hadesky.cacw.ui.view.UserInfoView;

import rx.Observable;

/**
 * 用户资料页
 * Created by dzysg on 2016/9/4 0004.
 */
public class UserInfoPresenterImpl implements UserInfoPresenter
{
    private UserRepertory mUserRepertory;
    private UserInfoView mView;
    private UserBean mUser;

    public UserInfoPresenterImpl(UserInfoView view, UserBean u)
    {
        mView = view;
        mUserRepertory = UserRepertory.getInstance();
        mUser = u;
    }

    @Override
    public void cancel()
    {

    }

    @Override
    public void loadUserInfo()
    {
        Observable<UserBean> observable;
        if (mUser.getId() == MyApp.getCurrentUser().getId())
        {
            observable = mUserRepertory.loadAndUpdateMyInfo(mUser.getUsername());
        } else
            observable = mUserRepertory.getUserInfo(mUser.getUsername());
        observable.subscribe(new RxSubscriber<UserBean>()
        {
            @Override
            public void _onError(String msg)
            {
                mView.showMsg(msg);
            }

            @Override
            public void _onNext(UserBean bean)
            {
                mView.showUserInfo(bean);
            }
        });
    }
}
