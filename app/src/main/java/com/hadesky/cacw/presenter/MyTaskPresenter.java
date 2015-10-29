package com.hadesky.cacw.presenter;

/**
 * Created by dzysg on 2015/10/29 0029.
 */
public interface MyTaskPresenter
{
    public void LoadTasks();
    public void CompleteTask(int taskid);
    public void DeleteTask(int taskid);

}
