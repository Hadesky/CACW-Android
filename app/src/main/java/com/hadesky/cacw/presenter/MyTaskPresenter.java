package com.hadesky.cacw.presenter;

/**控制任务页面数据加载逻辑
 * Created by dzysg on 2015/10/29 0029.
 */
public interface MyTaskPresenter
{
    public void LoadTasks();
    public void CompleteTask(int pos);
    public void DeleteTask(int pos);

}
