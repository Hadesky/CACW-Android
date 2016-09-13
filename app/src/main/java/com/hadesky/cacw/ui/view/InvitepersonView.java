package com.hadesky.cacw.ui.view;

import com.hadesky.cacw.bean.UserBean;

import java.util.List;

/**
 *
 * Created by dzysg on 2016/9/13 0013.
 */
public interface InvitePersonView extends BaseView
{
    void showUser(List<UserBean> list,boolean isfinal);
    void onInviteSucceed(int pos);
}
