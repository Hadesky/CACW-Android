package com.hadesky.cacw.model.network;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.TeamBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
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
    Observable<BaseResult<String>> login(@Body RequestBody body);


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

}
