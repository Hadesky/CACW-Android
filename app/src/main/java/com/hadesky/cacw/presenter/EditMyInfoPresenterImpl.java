package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.UserRepertory;
import com.hadesky.cacw.ui.view.EditMyInfoView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import rx.Subscription;

/** 个人资料编辑
 * Created by dzysg on 2016/9/4 0004.
 */
public class EditMyInfoPresenterImpl implements EditMyInfoPresenter
{

    private EditMyInfoView mView;
    private UserRepertory mUserRepertory;
    private Subscription mSubscription;
    private UserBean mUser;


    public EditMyInfoPresenterImpl(EditMyInfoView view)
    {
        mView = view;
        mUserRepertory = UserRepertory.getInstance();
        mUser = MyApp.getCurrentUser();
    }

    @Override
    public void updateAvatar(File avatar)
    {
        if(!avatar.exists())
        {
            mView.showMsg("文件不存在");
            return;
        }
        mView.showProgress();

        mUserRepertory.modifyUserIcon(avatar)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.hideProgress();
                    }
                    @Override
                    public void _onNext(String s)
                    {
                        mView.hideProgress();
                        mUser.setAvatarUrl(s);
                        mView.setAvatar(mUser.getAvatarUrl());
                    }
                });
    }

    @Override
    public void updateSexual(final int sex)
    {
        mView.showProgress();
        Map<String,String> map = new HashMap<>();
        map.put("sex",String.valueOf(sex));
        mSubscription =  mUserRepertory.modifyUserInfo(map)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.hideProgress();
                        mView.showMsg(msg);
                    }
                    @Override
                    public void _onNext(String s)
                    {
                        mView.hideProgress();
                        mView.setSex(sex);
                        mUser.setSex(sex);
                    }
                });
    }

    @Override
    public void updateSummary(final String summary)
    {
        mView.showProgress();
        Map<String,String> map = new HashMap<>();
        map.put("summary",summary);
        mSubscription =  mUserRepertory.modifyUserInfo(map)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.hideProgress();
                        mView.showMsg(msg);
                    }
                    @Override
                    public void _onNext(String s)
                    {
                        mView.hideProgress();
                        mView.setSummary(summary);
                        mUser.setSummary(summary);

                    }
                });
    }

    @Override
    public void updateNickName(final String nickName)
    {
        mView.showProgress();
        Map<String,String> map = new HashMap<>();
        map.put("nickName",nickName);
        mSubscription =  mUserRepertory.modifyUserInfo(map)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.hideProgress();
                        mView.showMsg(msg);
                    }
                    @Override
                    public void _onNext(String s)
                    {
                        mView.hideProgress();
                        mView.setNickName(nickName);
                        mUser.setNickName(nickName);
                    }
                });
    }

    @Override
    public void loadInfo()
    {
        mView.setAddress(mUser.getAddress());
        mView.setAvatar(mUser.getAvatarUrl());
        mView.setNickName(mUser.getNickName());
        mView.setPhoneNumber(mUser.getMobilePhone());
        mView.setSex(mUser.getSex());
        mView.setShortPhoneNumber(mUser.getShortNumber());
        mView.setSummary(mUser.getSummary());
        mView.setUserName(mUser.getUsername());
    }

    @Override
    public void updatePhone(final String mobilePhone)
    {
        mView.showProgress();
        Map<String,String> map = new HashMap<>();
        map.put("mobilePhone",mobilePhone);
        mSubscription =  mUserRepertory.modifyUserInfo(map)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.hideProgress();
                        mView.showMsg(msg);
                    }
                    @Override
                    public void _onNext(String s)
                    {
                        mView.hideProgress();
                        mView.setPhoneNumber(mobilePhone);
                        mUser.setMobilePhone(mobilePhone);
                    }
                });
    }

    @Override
    public void updateShortPhone(final String shortNumber)
    {
        mView.showProgress();
        Map<String,String> map = new HashMap<>();
        map.put("shortNumber",shortNumber);
        mSubscription =  mUserRepertory.modifyUserInfo(map)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.hideProgress();
                        mView.showMsg(msg);
                    }
                    @Override
                    public void _onNext(String s)
                    {
                        mView.hideProgress();
                        mView.setShortPhoneNumber(shortNumber);
                        mUser.setShortNumber(shortNumber);
                    }
                });
    }

    @Override
    public void updateAddress(final String address)
    {
        mView.showProgress();
        Map<String,String> map = new HashMap<>();
        map.put("address",address);
        mSubscription =  mUserRepertory.modifyUserInfo(map)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.hideProgress();
                        mView.showMsg(msg);
                    }
                    @Override
                    public void _onNext(String s)
                    {
                        mView.hideProgress();
                        mView.setAddress(address);
                        mUser.setAddress(address);
                    }
                });
    }

    @Override
    public void cancel()
    {
        if(mSubscription!=null&&!mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
        MyApp.getSessionManager().saveUser(mUser);
    }
}
