package com.hadesky.cacw.model;

import com.google.gson.JsonObject;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.DB.DatabaseManager;
import com.hadesky.cacw.model.network.CacwServer;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**团队数据仓库，包括新建团队、获取团队资料等
 * Created by dzysg on 2016/9/1 0001.
 */
public class TeamRepertory
{

    CacwServer mCacwServer;
    private static TeamRepertory sTeamRepertory;
    private DatabaseManager mDatabaseManager;

    public static TeamRepertory getInstance()
    {
        if (sTeamRepertory == null)
            sTeamRepertory = new TeamRepertory();
        return sTeamRepertory;
    }

    public TeamRepertory()
    {
        mCacwServer = MyApp.getApiServer();
        mDatabaseManager = DatabaseManager.getInstance(MyApp.getAppContext());
    }


    public Observable<String> createTeam(String name, File avatar)
    {

        MultipartBody.Part body = null;
        if (avatar != null)
        {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), avatar);
            body = MultipartBody.Part.createFormData("img", avatar.getName(), requestFile);
        }

        return mCacwServer.createTeam(name, body, "dex").subscribeOn(Schedulers.io()).compose(RxHelper.<String>handleResult()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<TeamBean>> getTeamList()
    {
        return mCacwServer.getTeamList(true).subscribeOn(Schedulers.io()).compose(RxHelper.<List<TeamBean>>handleResult()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<TeamBean> getTeamInfo(int tid)
    {
        return mCacwServer.getTeamInfo(tid).subscribeOn(Schedulers.io()).compose(RxHelper.<TeamBean>handleResult()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<UserBean>> getTeamMember(int tid, Integer limit, Integer offset, boolean all)
    {

        return mCacwServer.getTeamMember(tid, limit, offset, all)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<List<UserBean>>handleResult())
                .observeOn(AndroidSchedulers.mainThread());

    }


    public Observable<String> modifyTeamInfo(int tid, Map<String,String> map)
    {

        JsonObject jsonObject = new JsonObject();
        for(Map.Entry<String,String> entry:map.entrySet())
        {
            jsonObject.addProperty(entry.getKey(),entry.getValue());
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString());

       return  mCacwServer.modifyTeamInfo(tid,body)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<String> modifyTeamIcon(int tid,File file)
    {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part  body = MultipartBody.Part.createFormData("img", file.getName(), requestFile);

       return mCacwServer.modifyTeamIcon(tid, body)
               .subscribeOn(Schedulers.io())
               .compose(RxHelper.<String>handleResult())
               .observeOn(AndroidSchedulers.mainThread())
               ;

    }

    public Observable<List<ProjectBean>> getTeamProjects(int tid,Boolean isFile)
    {
       String state;
        if(isFile==null)
            state = null;
        else if(isFile)
            state = "file";
        else
            state = "unfile";

       return  mCacwServer.getTeamProjects(tid,state)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<List<ProjectBean>>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> createTeamProject(int tid,String pname)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("teamid",tid);
        jsonObject.addProperty("projectname",pname);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString());

       return  mCacwServer.createProject(body)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<String> removeTeamMember(int tid,int userid)
    {
        return mCacwServer.deleteTeamMember(tid,userid)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> exitTeam(int tid)
    {
        return  mCacwServer.exitTeam(tid)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<TeamBean>> searchTeam(String key,int limit,int offset)
    {
        return mCacwServer.searchTeam(key,limit,offset)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<List<TeamBean>>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> invitePerson(int tid,int uid)
    {
        return mCacwServer.inviteUser(tid,uid)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> applyTeam(int tid,String content)
    {
        return mCacwServer.applyTeam(tid,content)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> addTeamMember(int tid, final int uid)
    {
        return  mCacwServer.addTeamMember(tid,uid)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s)
                    {
                        mDatabaseManager.deleteMessageById(uid);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


}
