package com.hadesky.cacw.presenter;

import com.hadesky.cacw.JPush.JPushSender;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.TaskMember;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.tag.IntentTag;
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
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 编辑任务
 * Created by dzysg on 2016/7/6 0006.
 */
public class EditTaskPresenterImpl implements EditTaskPresenter
{
    TaskBean mTask;

    List<TaskMember> mMembers;
    List<TaskMember> mOldMembers;
    boolean newTask;
    UserBean mCurrentUser;
    EditTaskView mView;
    Subscription mSubscription;
    TaskBean mOldTask;


    public EditTaskPresenterImpl(EditTaskView view, TaskBean task, boolean isNewTask)
    {
        mTask = task;
        newTask = isNewTask;
        mView = view;
        mCurrentUser = MyApp.getCurrentUser();
        mTask.setAdaminUserId(MyApp.getCurrentUser().getObjectId());
        mOldTask = mTask.clone();
    }

    @Override
    public void loadTaskMember()
    {
        if (newTask)
        {
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

        tm.findObjects(new FindListener<TaskMember>()
        {
            @Override
            public void done(List<TaskMember> list, BmobException e)
            {
                mView.hideProgress();
                mOldMembers = new ArrayList<>(list);
                if (e == null)
                {
                    mView.showTaskMember(list);
                } else
                {
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
    private void createNewTask(final List<TaskMember> members)
    {

        mView.showProgress();
        final List<BmobObject> list = new ArrayList<>();
        list.addAll(members);

        mSubscription = mTask.saveObservable().observeOn(AndroidSchedulers.mainThread())

                .flatMap(new Func1<String, Observable<List<BmobObject>>>()
                {
                    @Override
                    public Observable<List<BmobObject>> call(String s)
                    {
                        return Observable.just(list);
                    }
                }).flatMap(new Func1<List<BmobObject>, Observable<List<BatchResult>>>()
                {
                    @Override
                    public Observable<List<BatchResult>> call(List<BmobObject> bmobObjects)
                    {
                        BmobBatch batch = new BmobBatch();
                        batch.insertBatch(bmobObjects);
                        return batch.doBatchObservable();
                    }
                })
                .subscribe(new Action1<List<BatchResult>>() {
                    @Override
                    public void call(List<BatchResult> batchResults)
                    {
                        BmobException error = null;
                        for(BatchResult br:batchResults)
                        {
                            if(br.getError() != null){
                                error = br.getError();
                                break;
                            }
                        }
                        mView.hideProgress();
                        if (error==null)
                        {
                            sendJoinTaskPush(members);
                            mView.showMsg("创建成功");
                            mView.closePage();
                        }else
                        {
                            mView.showMsg(error.getMessage());
                        }
                    }
                });
    }

    //通知被加入任务
    private void sendJoinTaskPush(List<TaskMember> members)
    {
        //去掉自己
        for(int i=0;i<members.size();i++)
        {
            if (members.get(i).getUser().equals(mCurrentUser))
                members.remove(i);
        }
        String title="新任务";
        String content = IntentTag.TAG_PUSH_TASK + mTask.getObjectId() + "你被加入任务 " + mTask.getTitle();

        JPushSender.SenderBuilder builder =new JPushSender.SenderBuilder().Message(title,content);
        for(TaskMember tm:members)
        {
            builder.addAlias(tm.getUser().getObjectId());
        }
        JPushSender sender = builder.build();
        MyApp.getJPushManager().sendMsg(sender,null);
    }


    //通知成员被移除
    private void sendRemoveTaskPush(List<TaskMember> members)
    {
        String title="任务信息";
        String content = "你已被移出任务 " + mTask.getTitle();
        JPushSender.SenderBuilder builder =new JPushSender.SenderBuilder().Message(title,content);
        for(TaskMember tm:members)
        {
            builder.addAlias(tm.getUser().getObjectId());
        }
        JPushSender sender = builder.build();
        MyApp.getJPushManager().sendMsg(sender,null);
    }

    //通知任务被修改
    private void sendTaskChangedPush(List<TaskMember> members)
    {
        //去掉自己
        for(int i=0;i<members.size();i++)
        {
            if (members.get(i).getUser().equals(mCurrentUser))
                members.remove(i);
        }

        String title="任务通知";
        String content = IntentTag.TAG_PUSH_TASK + mTask.getObjectId() + "任务 " + mOldTask.getTitle()+" 信息发生变化";
        JPushSender.SenderBuilder builder =new JPushSender.SenderBuilder().Message(title,content);
        for(TaskMember tm:members)
        {
            builder.addAlias(tm.getUser().getObjectId());
        }
        JPushSender sender = builder.build();
        MyApp.getJPushManager().sendMsg(sender,null);
    }

    private boolean checkTaskChange()
    {
        boolean same = mTask.getTitle().equals(mOldTask.getTitle());
        same &= mTask.getStartDate().equals(mOldTask.getStartDate());
        same &= mTask.getEndDate().equals(mOldTask.getEndDate());
        same &= mTask.getLocation().equals(mOldTask.getLocation());
        same &= mTask.getContent().equals(mOldTask.getContent());
        same &= mTask.getLocation().equals(mOldTask.getLocation());
        return !same;
    }


    //保存编辑后的任务
    private void updateTask(List<TaskMember> members)
    {
        if (mOldMembers == null)
        {
            mView.showMsg("请等待成员加载完成");
            return;
        }
        BmobBatch batch = new BmobBatch();
        final List<TaskMember> addlist = new ArrayList<>();
        final List<TaskMember> common = new ArrayList<>();


        //剔除相同的成员
        for(TaskMember tm : members)
        {
            if (mOldMembers.contains(tm))
            {
                mOldMembers.remove(tm);
                common.add(tm);//common是前后不变的成员
            } else
                addlist.add(tm);
        }

        //oldmember剩下的是要删除的
        if (mOldMembers.size() > 0)
        {
            List<BmobObject> del = new ArrayList<>();
            del.addAll(mOldMembers);
            batch.deleteBatch(del);
        }

        //addlist就是新增的
        if (addlist.size() > 0)
        {
            List<BmobObject> add = new ArrayList<>();
            add.addAll(addlist);
            batch.insertBatch(add);
        }

        List<BmobObject> task = new ArrayList<>();
        task.add(mTask);
        batch.updateBatch(task);


        mView.showProgress();
        batch.doBatch(new QueryListListener<BatchResult>()
        {
            @Override
            public void done(List<BatchResult> list, BmobException e)
            {
                mView.hideProgress();
                if (e == null)
                {
                    sendJoinTaskPush(addlist);
                    sendRemoveTaskPush(mOldMembers);
                    if (checkTaskChange())
                        sendTaskChangedPush(common);

                    mView.showMsg("保存成功");
                    mView.closePage();
                } else
                {
                    mView.showMsg(e.getMessage());
                }
            }
        });
    }


    @Override
    public void saveTask(List<TaskMember> members)
    {
        mMembers = members;
        if (mMembers == null)
            return;
        if (members.size() == 0)
        {
            mView.showMsg("数据异常，成员数量为0");
            return;
        }

        if (mTask.getProjectBean() == null)
        {
            mView.showMsg("请选择所属项目");
            return;
        }
        if (mTask.getTitle().length() == 0)
        {
            mView.showMsg("标题不可为空");
            return;
        }


        if (newTask)
        {
            createNewTask(members);
        } else
        {
            updateTask(members);
        }
    }

    @Override
    public void loadProjects()
    {

        mView.showProgress();
        BmobQuery<TeamMember> tm = new BmobQuery<>();
        tm.addWhereEqualTo("mUser", new BmobPointer(MyApp.getCurrentUser()));


        tm.findObjectsObservable(TeamMember.class).map(new Func1<List<TeamMember>, List<String>>()
        {
            @Override
            public List<String> call(List<TeamMember> list)
            {
                List<String> IdList = new ArrayList<>();
                for(TeamMember tm : list)
                {
                    IdList.add(tm.getTeam().getObjectId());
                }
                return IdList;
            }
        }).flatMap(new Func1<List<String>, Observable<List<ProjectBean>>>()
        {
            @Override
            public Observable<List<ProjectBean>> call(List<String> IdList)
            {

                BmobQuery<ProjectBean> pb = new BmobQuery<>();
                pb.include("mTeam");
                BmobQuery<TeamBean> tb = new BmobQuery<>();
                tb.addWhereContainedIn("objectId", IdList);
                pb.addWhereMatchesQuery("mTeam", "TeamBean", tb);
                return pb.findObjectsObservable(ProjectBean.class);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<ProjectBean>>()
        {
            @Override
            public void onCompleted()
            {
                mView.hideProgress();
            }

            @Override
            public void onError(Throwable throwable)
            {
                mView.showMsg(throwable.getMessage());
                mView.hideProgress();
            }

            @Override
            public void onNext(List<ProjectBean> projectBeens)
            {
                mView.selectProject(projectBeens);
            }
        });
    }

    @Override
    public void onDestroy()
    {
        if (mSubscription != null)
            mSubscription.unsubscribe();
    }

}
