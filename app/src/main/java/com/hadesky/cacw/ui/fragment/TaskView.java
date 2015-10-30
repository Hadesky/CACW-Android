package com.hadesky.cacw.ui.fragment;

import com.hadesky.cacw.bean.TaskBean;

import java.util.List;

/**我的任务页面接口
 * Created by dzysg on 2015/10/29 0029.
 */
public interface TaskView
{
    public void showDatas(List<TaskBean> tasks);
    public void showProgress();
    public void hideProgress();
    public void onFailure(String msg);
    public void showWaitingDialog(boolean is);

}
