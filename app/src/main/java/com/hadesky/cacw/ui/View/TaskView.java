package com.hadesky.cacw.ui.view;

import com.hadesky.cacw.bean.TaskBean;

import java.util.List;

/**我的任务页面接口
 * Created by dzysg on 2015/10/29 0029.
 */
public interface TaskView
{
     void showDatas(List<TaskBean> tasks);
     void showProgress();
     void hideProgress();
     void onFailure(String msg);
     void showWaitingDialog(boolean is);

}
