package com.hadesky.cacw.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.view.EditMyInfoView;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by MicroStudent on 2016/7/10.
 */

public class EditMyInfoPresenterImpl implements EditMyInfoPresenter {

    private static final String TAG = "EditMyInfoPresenterImpl";

    private EditMyInfoView mEditMyInfoView;
    private UserBean mCurrentUser;

    public EditMyInfoPresenterImpl(EditMyInfoView view) {
        mEditMyInfoView = view;
        mCurrentUser = MyApp.getCurrentUser();
    }

    /**
     * 这里做两步，第一步先删除服务器上的头像，第二部修改
     *
     * @param file 已经上传完成的头像文件
     */
    private void updateUserAvatar(final BmobFile file) {
        BmobFile oldFile = MyApp.getCurrentUser().getUserAvatar();
        if (oldFile != null) {
            oldFile.delete();
        }

        UserBean newBean = new UserBean();
        newBean.setUserAvatar(file);
        newBean.update(MyApp.getCurrentUser().getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mEditMyInfoView.hideProgress();
                    mEditMyInfoView.setAvatar(file.getUrl());
                }
            }
        });
    }

    /**
     * 这里只做第一步，上传图片，真正更改用户头像的操作在updateUserAvatar里
     * @param avatarPath 头像的路径
     */
    @Override
    public void updateAvatar(String avatarPath) {
        mEditMyInfoView.showProgress();
        final BmobFile avatar = new BmobFile(new File(avatarPath));
        avatar.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    updateUserAvatar(avatar);
                } else {
                    mEditMyInfoView.hideProgress();
                    mEditMyInfoView.showMsg("图片上传失败，请检查网络！");
                }
            }
        });
    }

    @Override
    public void updateSexual(final Byte sex) {
        UserBean newUser = new UserBean();
        newUser.setSex(sex);
        UserBean currentUser = BmobUser.getCurrentUser(UserBean.class);

        newUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mEditMyInfoView.setSex(sex);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void updateSummary(final String summary) {
        UserBean newUser = new UserBean();
        newUser.setSummary(summary);
        UserBean currentUser = BmobUser.getCurrentUser(UserBean.class);

        newUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mEditMyInfoView.setSummary(summary);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void updateNickName(final String nickName) {
        if (nickName == null || nickName.isEmpty()) {
            mEditMyInfoView.showMsg("不能设置为空");
            return;
        }
        UserBean newUser = new UserBean();
        newUser.setNickName(nickName);
        UserBean currentUser = BmobUser.getCurrentUser(UserBean.class);

        newUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mEditMyInfoView.setNickName(nickName);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadInfo() {
        if (mCurrentUser != null) {
            mEditMyInfoView.setSex(mCurrentUser.getSex());
            mEditMyInfoView.setNickName(mCurrentUser.getNickName());
            mEditMyInfoView.setSummary(mCurrentUser.getSummary());
            mEditMyInfoView.setUserName(mCurrentUser.getUsername());
            if (mCurrentUser.getUserAvatar()!=null)
            mEditMyInfoView.setAvatar(mCurrentUser.getUserAvatar().getUrl());
            mEditMyInfoView.setPhoneNumber(mCurrentUser.getMobilePhoneNumber());
            mEditMyInfoView.setShortPhoneNumber(mCurrentUser.getShortNumber());
            mEditMyInfoView.setAddress(mCurrentUser.getAddress());
        }
    }

    @Override
    public void updatePhone(final String phoneNumber) {
        UserBean newUser = new UserBean();
        newUser.setMobilePhoneNumber(phoneNumber);
        UserBean currentUser = BmobUser.getCurrentUser(UserBean.class);

        newUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mEditMyInfoView.setPhoneNumber(phoneNumber);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void updateShortPhone(final String shortPhone) {
        UserBean newUser = new UserBean();
        newUser.setShortNumber(shortPhone);
        UserBean currentUser = BmobUser.getCurrentUser(UserBean.class);

        newUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mEditMyInfoView.setShortPhoneNumber(shortPhone);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void updateAddress(final String address) {
        UserBean newUser = new UserBean();
        newUser.setAddress(address);
        UserBean currentUser = BmobUser.getCurrentUser(UserBean.class);

        newUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mEditMyInfoView.setAddress(address);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
