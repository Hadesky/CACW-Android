package com.hadesky.cacw.Model;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.database.MyTaskDAO;

import java.util.List;

/**MyTask这个页面的数据层,负责从本地或网络获取数据
 * Created by dzysg on 2015/10/29 0029.
 */
public class MyTaskModel
{
    //必须在主线程调用这个接口
    private GetDateCallBack mCallBack;
    private MyTaskDAO mDAO = new MyTaskDAO();
    public MyTaskModel(GetDateCallBack callBack)
    {
        mCallBack = callBack;
    }

    public void LoadTaskByCache()
    {
        List<TaskBean> list = mDAO.getUnCompleteTask();
        mCallBack.onSucceed(list);
    }

    public void LoadTaskByNetwork()
    {

    }

    public void deleteTask(int id)
    {

    }


    public interface GetDateCallBack{
        public void onSucceed(List<TaskBean> list);
        public void onFalure(String error);
    }
}
