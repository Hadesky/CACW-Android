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
        tm.include("mUser");

        tm.findObjects(new FindListener<TaskMember>() {
            @Override
            public void done(List<TaskMember> list, BmobException e) {
                mView.hideProgress();
                mOldMembers = list;
                if (e == null) {
                    mView.showTaskMember(mOldMembers);
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
        if (mOldMembers==null)
        {
            mView.showMsg("请等待成员加载完成");
            return ;
        }



        BmobBatch batch = new BmobBatch();
        List<TaskMember> addlist = new ArrayList<>();

        //剔除相同的成员
        for(TaskMember tm:members)
        {
            if (mOldMembers.contains(tm))
            {
                mOldMembers.remove(tm);

            }else
                addlist.add(tm);
        }

        //oldmember剩下的是要删除的

        if (mOldMembers.size()>0)
        {
            List<BmobObject> del = new ArrayList<>();
            del.addAll(mOldMembers);
            batch.deleteBatch(del);
        }
        //addlist就是新增的
        if (addlist.size()>0)
        {
            List<BmobObject> add = new ArrayList<>();
            add.addAll(addlist);
            batch.insertBatch(add);
        }


        List<BmobObject> task = new ArrayList<>();
        task.add(mTask);
        batch.updateBatch(task);


        mView.showProgress();
        batch.doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                mView.hideProgress();
                if (e==null)
                {
                    mView.showMsg("保存成功");
                    mView.closePage();
                }else
                {
                    mView.showMsg(e.getMessage());
                }
            }
        });
    }


    @Override
    public void saveTask(List<TaskMember> members) {
        mMembers = members;
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
