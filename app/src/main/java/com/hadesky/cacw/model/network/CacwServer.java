package com.hadesky.cacw.model.network;

import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 *
 * Created by dzysg on 2016/8/31 0031.
 */
public interface CacwServer
{





    @POST("/v1/account/register")
    Observable<BaseResult<String>> register(@Body RequestBody body);


    @POST("/v1/account/login")
    Observable<BaseResult<UserBean>> login(@Body RequestBody body);


    @GET("/v1/account/logout")
    Observable<BaseResult<String>> logout();


    @GET("/v1/task/list")
    Observable<BaseResult<List<TaskBean>>> getTaskList(@Query("state") String state);


    //第三个参数暂时无用
    @POST("/v1/team/create/{teamname}")
    @Multipart()
    Observable<BaseResult<String>> createTeam(@Path("teamname")String name,
                                              @Part MultipartBody.Part file,
                                              @Part("des") String des);


    @GET("/v1/team/list")
    Observable<BaseResult<List<TeamBean>>> getTeamList(@Query("allcolumn") boolean all);


    @GET("/v1/team/{tid}")
    Observable<BaseResult<TeamBean>> getTeamInfo(@Path("tid") int tid);


    @GET("/v1/team/{tid}/members")
    Observable<BaseResult<List<UserBean>>> getTeamMember(
            @Path("tid") int tid,
            @Query("limit") Integer limit,
            @Query("offset") Integer offset,
            @Query("allcolumn") boolean allcolumn
    );


    @POST("/v1/team/{tid}")
    Observable<BaseResult<String>> modifyTeamInfo(@Path("tid") int tid, @Body RequestBody body);


    //第三个参数暂时无用
    @POST("/v1/team/{tid}/upload")
    @Multipart()
    Observable<BaseResult<String>> modifyTeamIcon(@Path("tid")int tid,
                                              @Part MultipartBody.Part file);

    @GET("/v1/team/{tid}/projectlist")
    Observable<BaseResult<List<ProjectBean>>> getTeamProjects(@Path("tid")int tid,@Query("state") String state);

    @POST("/v1/project/create")
    Observable<BaseResult<String>> createProject(@Body RequestBody body);

    @GET("/v1/task/{tid}/members")
    Observable<BaseResult<List<UserBean>>> getTaskMembers(@Path("tid")int tid);

    @POST("/v1/task/create")
    Observable<BaseResult<TaskBean>> createTask(@Body RequestBody body);

    @GET("/v1/project/list")
    Observable<BaseResult<List<ProjectBean>>> getProjectList(
            @Query("type") String type, @Query("state") String state);


    @POST("/v1/task/{tid}")
    Observable<BaseResult<String>> modifyTaskInfo(@Path("tid") int tid, @Body RequestBody body);

    @DELETE("/v1/task/{tid}")
    Observable<BaseResult<String>> deleteTask(@Path("tid") int tid);

    @GET("/v1/project/{pid}")
    Observable<BaseResult<ProjectBean>> getProject(@Path("pid") int pid);

    @POST("/v1/task/{tid}/members")
    Observable<BaseResult<String>> addTaskMember(@Path("tid") int id,@Body RequestBody body);

    @POST("/v1/task/{tid}/DeleteMembers ")
    Observable<BaseResult<String>> deleteTaskMember(@Path("tid") int id,@Body RequestBody body);


    @GET("/v1/user/{username}")
    Observable<BaseResult<UserBean>> getUserInfo(@Path("username") String username);


    @POST("/v1/user")
    Observable<BaseResult<String>> modifyUserInfo(@Body RequestBody body);


    @POST("/v1/user/upload")
    @Multipart()
    Observable<BaseResult<String>> modifyUserIcon(@Part MultipartBody.Part file);


    @DELETE("/v1/team/{tid}/members")
    Observable<BaseResult<String>> deleteTeamMember(@Path("tid") int tid, @Query(
            "memberid") int userid);


    @POST("/v1/team/{tid}/members")
    Observable<BaseResult<String>> addTeamMember(@Path("tid") int tid, @Query(
            "memberid") int userid);


    @GET("/v1/team/{tid}/out")
    Observable<BaseResult<String>> exitTeam(@Path("tid") int tid);

    @GET("/v1/project/{pid}/tasklist")
    Observable<BaseResult<List<TaskBean>>> getProjectTask(@Path("pid") int tid,@Query("state") String state);



    @GET("/v1/task/{tid}/finish")
    Observable<BaseResult<String>> finishTask(@Path("tid") int tid);



    @GET("/v1/user/search")
    Observable<BaseResult<List<UserBean>>> searchUser(@Query("query") String text,@Query("limit")int limit,@Query("offset")int offset,@Query("teamid") Integer teamid);


    @GET("/v1/team/search")
    Observable<BaseResult<List<TeamBean>>> searchTeam(@Query("query") String text,@Query("limit")int limit,@Query("offset")int offset);

    @GET("/v1/team/{tid}/invite")
    Observable<BaseResult<String>> inviteUser(@Path("tid")int tid,@Query("uid")int uid);

    @GET("/v1/team/apply")
    Observable<BaseResult<String>> applyTeam(@Query("tid")int tid,@Query("content")String content);

    @GET("/v1/message")
    Observable<BaseResult<List<MessageBean>>> getMessages();

    @POST("/v1/message")
    Observable<BaseResult<String>> sendMessage(@Body RequestBody body);

}
