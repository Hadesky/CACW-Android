package com.hadesky.cacw.ui.view;

import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.UserBean;

import java.util.List;

/**
 *
 * Created by dzysg on 2016/7/6 0006.
 */
public interface EditTaskView extends BaseView{

    void showTaskMember(List<UserBean> members);

    void closePage();

    void selectProject(List<ProjectBean> projectBeen);

    void showTaskDetail(TaskBean b);

}
