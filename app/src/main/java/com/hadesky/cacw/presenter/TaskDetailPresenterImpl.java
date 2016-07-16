package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.TaskMember;
import com.hadesky.cacw.ui.view.TaskDetailView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;

/**任务详情操作接口实现 {@link TaskDetailPresenter}
 * Created by dzysg on 2015/11/14 0014.
 */
public class TaskDetailPresenterImpl implements TaskDetailPresenter
{


    private TaskDetailView mView;
    private TaskBean mTask;
    private Subscription mSubscription;


    public TaskDetailPresenterImpl(TaskDetailView view,TaskBean task)
    {
        mTask = task;
        mView = view;
    }
    

    @Override
    public void LoadTaskMember()
    {
        mView.showProgress();
        BmobQuery<TaskMember> query = new BmobQuery<>();
        query.addWhereEqualTo("mTask", new BmobPointer(mTask));
        query.include("mUser");
        query.findObjects(new FindListener<TaskMember>() {
            @Override
            public void done(List<TaskMember> list, BmobException e) {
                mView.hideProgress();
                if (e != null)
                    mView.showMsg(e.getMessage());
                else
                {
                    mView.ShowMember(list);
                }
            }
        });
    }

    @Override
    public void onDestroy() {

    }
}
