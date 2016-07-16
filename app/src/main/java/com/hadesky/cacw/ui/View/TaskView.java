package com.hadesky.cacw.ui.view;

import com.hadesky.cacw.bean.TaskMember;

import java.util.List;

/**我的任务页面接口
 * Created by dzysg on 2015/10/29 0029.
 */
public interface TaskView extends BaseView
{
     void showDatas(List<TaskMember> tasks);
}
