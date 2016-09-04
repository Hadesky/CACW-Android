package com.hadesky.cacw.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.network.BaseResult;
import com.hadesky.cacw.model.network.CacwServer;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/** 任务数据仓库
 * Created by dzysg on 2016/8/31 0031.
 */
public class TaskRepertory
{

    CacwServer mCacwServer;

    private static TaskRepertory sTaskRepertory;


    public static TaskRepertory getInstance()
    {
        if (sTaskRepertory == null)
            sTaskRepertory = new TaskRepertory();
        return sTaskRepertory;
    }


    public TaskRepertory()
    {
        mCacwServer = MyApp.getApiServer();
    }

    /**
     * 获取任务列表
     *
     * @param state 0 为未完成 1为完成 2 为所有
     * @return 任务列表Observable
     */
    public Observable<List<TaskBean>> getTaskList(int state)
    {
        String query;
        switch (state)
        {
            case 0:
                query = "unfinish";
                break;
            case 1:
                query = "finished";
                break;
            default:
                query = "all";
        }
        return mCacwServer.getTaskList(query).subscribeOn(Schedulers.io()).compose(RxHelper.<List<TaskBean>>handleResult()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<UserBean>> getTaskMember(int tid)
    {
        return mCacwServer.getTaskMembers(tid).subscribeOn(Schedulers.io()).compose(RxHelper.<List<UserBean>>handleResult()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<TaskBean> createTask(TaskBean task, List<UserBean> member)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", task.getTitle());
        jsonObject.addProperty("content", task.getContent());
        jsonObject.addProperty("location", task.getLocation());
        jsonObject.addProperty("projectId", task.getProject().getId());
        jsonObject.addProperty("startDate", task.getStartDate());
        jsonObject.addProperty("endDate", task.getEndDate());

        JsonArray jsonArray = new JsonArray();
        for(UserBean ub : member)
        {
            jsonArray.add(ub.getId());
        }
        jsonObject.add("members", jsonArray);

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        return mCacwServer.createTask(body).subscribeOn(Schedulers.io()).compose(RxHelper.<TaskBean>handleResult()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<String> modifyTaskInfo(TaskBean task)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", task.getTitle());
        jsonObject.addProperty("content", task.getContent());
        jsonObject.addProperty("location", task.getLocation());
        jsonObject.addProperty("startDate", task.getStartDate());
        jsonObject.addProperty("endDate", task.getEndDate());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        return mCacwServer.modifyTaskInfo(task.getId(), body).subscribeOn(Schedulers.io()).compose(RxHelper.<String>handleResult()).observeOn(AndroidSchedulers.mainThread());

    }


    public Observable<String> deleteTask(int tid)
    {
        return mCacwServer.deleteTask(tid).subscribeOn(Schedulers.io()).compose(RxHelper.<String>handleResult()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<UserBean>> getTeamMember(int pid)
    {
                //获取项目信息
                return  mCacwServer.getProject(pid)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseResult<ProjectBean>, Observable<BaseResult<List<UserBean>>>>()
                {
                    @Override
                    public Observable<BaseResult<List<UserBean>>> call(BaseResult<ProjectBean> projectBeanBaseResult)
                    {
                        if (projectBeanBaseResult.getState_code() == 0)
                            return mCacwServer.getTeamMember(projectBeanBaseResult.getData().getTeam().getId(), null, null, false);
                        else
                            return Observable.error(new RuntimeException(projectBeanBaseResult.getError_msg()));
                    }
                })
                .compose(RxHelper.<List<UserBean>>handleResult())
                        .observeOn(AndroidSchedulers.mainThread());
    }



    public Observable<String> addTaskMembers(int tid,List<UserBean> member)
    {

        JsonArray jsonArray = new JsonArray();
        for(UserBean ub:member)
        {
            jsonArray.add(ub.getId());
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonArray.toString());
        return mCacwServer.addTaskMember(tid,body)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
