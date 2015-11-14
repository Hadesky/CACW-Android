package com.hadesky.cacw.Model;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.database.TaskDetailDAO;

/**任务详情数据层
 * Created by dzysg on 2015/11/14 0014.
 */
public class TaskDetailModel
{
    TaskDetailCallBack mCallBack;
    TaskDetailDAO mDAO;

    public TaskDetailModel(TaskDetailCallBack callBack)
    {
        mCallBack = callBack;
        mDAO = new TaskDetailDAO();
    }

    public void getTask(long id)
    {
         TaskBean task =  mDAO.getTask(id);
         mCallBack.succeed(task);
    }


    public interface TaskDetailCallBack
    {
         void succeed(Object re);
         void error(String msg);
    }

}
