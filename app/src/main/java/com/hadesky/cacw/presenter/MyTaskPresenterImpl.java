package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TaskMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.view.TaskView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscription;

/**
 * 控制任务页面数据加载逻辑impl
 * Created by dzysg on 2015/10/29 0029.
 */
public class MyTaskPresenterImpl implements MyTaskPresenter {
    private TaskView mTaskView;
    private  List<TaskMember> mMemberLists;
    private   UserBean mUser;
    private  Subscription mSubscription;
    private boolean mIsFinished;
    public MyTaskPresenterImpl(TaskView view,boolean isFinished) {
        mTaskView = view;
        mUser = MyApp.getCurrentUser();
        mIsFinished = isFinished;
    }



    @Override
    public void LoadTasks() {

        mTaskView.showProgress();
        BmobQuery<TaskMember> query = new BmobQuery<>();
        query.addWhereEqualTo("mUser", new BmobPointer(mUser));
        if (mIsFinished)
            query.addWhereEqualTo("mIsFinish",2);
        else
            query.addWhereEqualTo("mIsFinish",1);

        query.include("mTask.mProjectBean");
        mSubscription =  query.findObjects(new FindListener<TaskMember>() {
            @Override
            public void done(List<TaskMember> list, BmobException e) {
                mTaskView.hideProgress();
                if (e==null)
                {
                    mTaskView.showDatas(list);
                    mMemberLists = list;
                }
                else
                {
                    mTaskView.showMsg(e.getMessage());

                }
            }
        });
    }


    @Override
    public void CompleteTask(final TaskMember tm) {
        mTaskView.showProgress();
        TaskMember t = new TaskMember();
        t.setObjectId(tm.getObjectId());
        t.setFinish(true);
        t.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                mTaskView.hideProgress();
                if (e==null)
                {
                    mMemberLists.remove(tm);
                    mTaskView.showMsg(tm.getTask().getTitle()+"任务已完成");
                    mTaskView.showDatas(mMemberLists);
                }else
                {
                    mTaskView.showMsg(e.getMessage());
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        if (mSubscription!=null)
            mSubscription.unsubscribe();
    }
}
