package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.UserBean;

import java.util.List;

/** 选择任务成员
 * Created by dzysg on 2016/9/3 0003.
 */
public interface SelectMemberPresenter
{
    void onCancel();
    void getSelectableMember();
    void addTaskMember(List<UserBean> newMember);
}
