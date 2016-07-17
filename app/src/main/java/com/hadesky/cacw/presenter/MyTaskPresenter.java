package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TaskMember;

/**控制任务页面数据加载逻辑接口
 * Created by dzysg on 2015/10/29 0029.
 */
public interface MyTaskPresenter
{
     void LoadTasks();
     void CompleteTask(TaskMember pos);

    void onDestroy();

}
