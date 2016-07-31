package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.UserBean;

import java.util.List;

/**
 *
 * Created by MicroStudent on 2016/7/16.
 */

public interface TeamMemberPresenter {
    void loadData();

    void onDestroy();

    int getUserCount();

    void deleteMember(UserBean bean);

    List<UserBean> getData();
}
