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
import cn.bmob.v3.listener.SaveListener;
import rx.Subscription;

/**
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


    /**
     * 保存新建任务
     *
     * @param members 成员
     */
    private void createNewTask(final List<TaskMember> members) {

        mView.showProgress();


        final List<BmobObject> list = new ArrayList<>();
        for (TaskMember tm : members) {
            list.add(tm);
        }

        mTask.setAdaminUserId(mCurrentUser.getObjectId());
        mSubscription = mTask.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e != null) {
                    mView.hideProgress();
                    mView.showMsg(e.getMessage());
                } else {
                    BmobBatch batch = new BmobBatch();

                    batch.insertBatch(list);
                   mSubscription =  batch.doBatch(new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> list, BmobException e) {
                        mView.hideProgress();
                            if (e==null)
                            {
                                mView.showMsg("创建成功");
                                mView.closePage();
                            }
                            else
                                mView.showMsg(e.getMessage());
                        }
                    });
                }
            }
        });


    }


    //保存编辑后的任务
    private void updateTask(List<TaskMember> members) {
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

        mSubscription = batch.doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                mView.hideProgress();
                if (e == null) {
                    mView.showMsg("保存成功");
                    mView.closePage();
                } else {
                    mView.showMsg(e.getMessage());
                }
            }
        });

    }


    @Override
    public void saveTask(List<TaskMember> members) {
        // TODO: 2016/7/9 0009 没测试
        if (mMembers == null)
            return;
        if (members.size() == 0) {
            mView.showMsg("数据异常，成员数量为0");
            return;
        }

        if (mTask.getProjectBean() == null) {
            mView.showMsg("请选择所属项目");
            return;
        }
        if (mTask.getTitle().length() == 0) {
            mView.showMsg("标题不可为空");
            return;
        }


        if (newTask) {
            createNewTask(members);
        } else {
            updateTask(members);
        }
    }

    @Override
    public void loadProjects() {

        mView.showProgress();
        BmobQuery<TeamMember> tm = new BmobQuery<>();
        tm.addWhereEqualTo("mUser", new BmobPointer(MyApp.getCurrentUser()));
        mSubscription = tm.findObjects(new FindListener<TeamMember>() {
            @Override
            public void done(List<TeamMember> list, BmobException e) {
                if (e != null) {
                    mView.hideProgress();
                    mView.showMsg(e.getMessage());
                } else {
                    List<String> IdList = new ArrayList<>();
                    for (TeamMember tm : list) {
                        IdList.add(tm.getTeam().getObjectId());
                    }
                    BmobQuery<ProjectBean> pb = new BmobQuery<>();
                    pb.include("mTeam");
                    BmobQuery<TeamBean> tb = new BmobQuery<>();
                    tb.addWhereContainedIn("objectId", IdList);
                    pb.addWhereMatchesQuery("mTeam", "TeamBean", tb);
                    pb.findObjects(new FindListener<ProjectBean>() {
                        @Override
                        public void done(List<ProjectBean> list, BmobException e) {
                            mView.hideProgress();
                            if (e != null) {

                                mView.showMsg(e.getMessage());
                            } else {
                                mView.selectProject(list);
                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null)
            mSubscription.unsubscribe();
    }

}
