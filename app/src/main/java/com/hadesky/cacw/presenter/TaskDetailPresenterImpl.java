package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.TaskMember;
import com.hadesky.cacw.ui.view.TaskDetailView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import rx.Subscription;

/**
 * 任务详情操作接口实现 {@link TaskDetailPresenter}
 * Created by dzysg on 2015/11/14 0014.
 */
public class TaskDetailPresenterImpl implements TaskDetailPresenter {


    private TaskDetailView mView;
    private TaskBean mTask;
    private List<TaskMember> mTaskMembers;
    private Subscription mSubscription;


    public TaskDetailPresenterImpl(TaskDetailView view, TaskBean task) {
        mTask = task;
        mView = view;
    }


    @Override
    public void LoadTaskMember() {
        mView.showProgress();
        BmobQuery<TaskMember> query = new BmobQuery<>();
        query.addWhereEqualTo("mTask", new BmobPointer(mTask));
        query.include("mUser");
        mSubscription = query.findObjects(new FindListener<TaskMember>() {
            @Override
            public void done(List<TaskMember> list, BmobException e) {
                mView.hideProgress();
                if (e != null)
                    mView.showMsg(e.getMessage());
                else {
                    mTaskMembers = list;
                    mView.ShowMember(list);
                }
            }
        });
    }
    @Override
    public void loadTaskInfo()
    {

        mView.showProgress();
        BmobQuery<TaskBean> query = new BmobQuery<>();
        query.include("mProjectBean");
        query.getObject(mTask.getObjectId(), new QueryListener<TaskBean>()
        {
            @Override
            public void done(TaskBean taskBean, BmobException e)
            {
                mView.hideProgress();
                if (e==null)
                {
                    mTask= taskBean;
                    mView.showInfo(mTask);
                    LoadTaskMember();
                }else
                {
                    mView.showMsg(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null)
            mSubscription.unsubscribe();
    }

    @Override
    public void onDeleteTask() {
        if (mTaskMembers==null)
        {
            mView.showMsg("未加载任务成员");
            return;
        }

        List<BmobObject> del = new ArrayList<>();
        for (TaskMember tm:mTaskMembers)
        {
            del.add(tm);
        }
        del.add(mTask);
        BmobBatch batch = new BmobBatch();
        mView.showProgress();
        batch.deleteBatch(del);
        batch.doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                mView.hideProgress();
                if (e == null) {
                    mView.closeActivity();
                } else {
                    mView.showMsg(e.getMessage());
                }
            }
        });

    }
}
