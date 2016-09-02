package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.UserBean;

/**
 *
 * Created by MicroStudent on 2016/7/16.
 */

public interface TeamMemberPresenter {
    void loadMembers();

    void onDestroy();

    int getUserCount();

    void deleteMember(UserBean bean);

}
