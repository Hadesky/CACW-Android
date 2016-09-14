package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.TaskRepertory;
import com.hadesky.cacw.ui.view.SelectTaskMemberView;

import java.util.Iterator;
import java.util.List;

import rx.Subscription;

/**
 * Created by dzysg on 2016/9/3 0003.
 */
public class SelectMemberPresenterImpl implements SelectMemberPresenter
{


    private SelectTaskMemberView mView;
    private TaskRepertory mRepertory;
    private List<UserBean> mCurrentUser;
    private int mTaskId;
    private int mProjectId;
    private Subscription mSubscription;


    public SelectMemberPresenterImpl(SelectTaskMemberView view, List<UserBean> Users, int pid,int tid)
    {
        mView = view;
        mRepertory = TaskRepertory.getInstance();
        mCurrentUser = Users;
        mProjectId = pid;
        mTaskId = tid;
    }

    @Override
    public void onCancel()
    {
        if(mSubscription!=null&&!mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

    @Override
    public void getSelectableMember()
    {
        mView.showProgress();
       mSubscription = mRepertory.getTeamMember(mProjectId).subscribe(new RxSubscriber<List<UserBean>>()
        {
            @Override
            public void _onError(String msg)
            {
                mView.hideProgress();
                mView.showMsg(msg);
            }
            @Override
            public void _onNext(List<UserBean> userBeen)
            {
                mView.hideProgress();
                if (userBeen != null)
                    handleResult(userBeen);
            }
        });
    }

    @Override
    public void addTaskMember(List<UserBean> newMember)
    {
        if(newMember.size()==0)
        {
            mView.FinishAndClose();
            return;
        }


        mView.showProgress();
        mSubscription= mRepertory.addTaskMembers(mTaskId,newMember)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.hideProgress();
                        mView.showMsg(msg);
                    }
                    @Override
                    public void _onNext(String s)
                    {
                        mView.hideProgress();
                        mView.showMsg("添加成功");
                        mView.FinishAndClose();
                    }
                });
    }

    private void handleResult(List<UserBean> all)
    {
        Iterator<UserBean> iterator = all.iterator();
        while (iterator.hasNext())
        {
             if(mCurrentUser.contains(iterator.next()))
             {
                 iterator.remove();
             }
        }
        mView.showMembers(all);
        if(all.size()==0)
            mView.showMsg("当前无成员可邀请");

    }
}
