package com.hadesky.cacw.ui.view;

import com.hadesky.cacw.bean.UserBean;

import java.util.List;

/**
 * Created by dzysg on 2016/9/3 0003.
 */
public interface SelectTaskMemberView  extends BaseView
{
    void showMembers(List<UserBean> userBeens);
    void FinishAndClose();
}
