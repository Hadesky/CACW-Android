package com.hadesky.cacw.ui.view;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.UserBean;

import java.util.List;

/**任务详情界面接口
 * Created by dzysg on 2015/11/14 0014.
 */
public interface TaskDetailView extends BaseView
{
     void showInfo(TaskBean task);
     void ShowMember(List<UserBean> user);
}
