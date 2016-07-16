package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.TaskMember;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.view.EditTaskView;

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
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 * Created by dzysg on 2016/7/6 0006.
 */
public class EditTaskPresenterImpl implements EditTaskPresenter {


    TaskBean mTask;

    List<TaskMember> mMembers;
    List<TaskMember> mOldMembers;
    boolean newTask;
    UserBean mCurrentUser;
    EditTaskView mView;
    Subscription mSubscription;


    public EditTaskPresenterImpl(EditTaskView view, TaskBean task, boolean isNewTask) {
        mTask = task;
        newTask = isNewTask;
        mView = view;
        mCurrentUser = MyApp.getCurrentUser();
    }

    @Override
    public void loadTaskMember() {
        if (newTask) {

            mMembers = new ArrayList<>();
            TaskMember tm = new TaskMember();
            tm.setTask(mTask);
            tm.setUser(mCurrentUser);
            mMembers.add(tm);
            mView.showTaskMember(mMembers);
            return;
        }

        mView.showProgress();
        BmobQuery<TaskMember> tm = new BmobQuery<>();
        tm.addWhereEqualTo("mTask", new BmobPointer(mTask));
        tm.include("mTask,mUser");

        tm.findObjects(new FindListener<TaskMember>() {
            @Override
            public void done(List<TaskMember> list, BmobException e) {
                mView.hideProgress();
                mOldMembers = list;
                if (e == null) {
                    mView.showTaskMember(mMembers);
                } else {
                    mView.showMsg(e.getMessage());
                }

            }
        });

    }


    /** 保存新建任务
     * @param members
     */
    private void CreateNewTask(List<TaskMember> members)
    {

        mView.showProgress();

        BmobBatch bmobBatch= new BmobBatch();
        List<BmobObject> tasklist = new ArrayList<>();
        tasklist.add(mTask);
        tasklist.addAll(members);

        bmobBatch.insertBatch(tasklist);
        mSubscription =  bmobBatch.doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                mView.hideProgress();
                if (e==null)
                {
                    mView.showMsg("新建任务成功");
                    mView.closePage();
                }else
                {
                    mView.showMsg(e.getMessage());
                }
            }
        });


    }

    //保存编辑后的任务
    private void updateTask(List<TaskMember> members)
    {
        mView.showProgress();
        BmobBatch batch = new BmobBatch();

        //先删除所有成员
        List<BmobObject> list = new ArrayList<>();
        list.addAll(mOldMembers);
        batch.deleteBatch(list);

        //再将编辑后的成员加回来
        List<BmobObject> newMember = new ArrayList<>();
        newMember.addAll(members);
        batch.insertBatch(newMember);

        mSubscription =  batch.doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                mView.hideProgress();
                if (e==null)
                {
                    mView.showMsg("保存成功");
                    mView.closePage();
                }
                else
                {
                    mView.showMsg(e.getMessage());
                }
            }
        });

    }


    @Override
    public void saveTask(List<TaskMember> members) {
        // TODO: 2016/7/9 0009 没测试
        if (mMembers==null)
            return;
        if (members.size()==0)
        {
            mView.showMsg("数据异常，成员数量为0");
            return;
        }

        if (mTask.getProjectBean()==null)
        {
            mView.showMsg("请选择所属项目");
            return;
        }


        if (newTask) {
           CreateNewTask(members);
        } else {
           updateTask(members);
        }
    }

    @Override
    public void loadProjects() {
        // TODO: 2016/7/9 0009 没测试
        mView.showProgress();
        BmobQuery<TeamMember> tm = new BmobQuery<>();
        tm.addWhereEqualTo("mUser", new BmobPointer(mCurrentUser));

        mSubscription =  tm.findObjectsObservable(TeamMember.class)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<TeamMember>, rx.Observable<List<ProjectBean>>>() {
            @Override
            public rx.Observable<List<ProjectBean>> call(List<TeamMember> teamMembers) {
                List<String> teamid = new ArrayList<>();
                for(TeamMember tm:teamMembers)
                {
                    teamid.add(tm.getTeam().getObjectId());
                }
                BmobQuery<TeamBean> teamQuery = new BmobQuery<>();
                BmobQuery<ProjectBean> query = new BmobQuery<>();
                teamQuery.addWhereContainedIn("ObjectId",teamid);
                query.addWhereMatchesQuery("mTeam", "TeamBean", teamQuery);
                return query.findObjectsObservable(ProjectBean.class);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ProjectBean>>() {
            @Override
            public void onCompleted() {
                mView.hideProgress();
            }

            @Override
            public void onError(Throwable throwable) {
                mView.hideProgress();
                mView.showMsg("当前没有项目");
            }

            @Override
            public void onNext(List<ProjectBean> projectBeen) {
                mView.selectProject(projectBeen);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mSubscription!=null)
        mSubscription.unsubscribe();
    }

}
