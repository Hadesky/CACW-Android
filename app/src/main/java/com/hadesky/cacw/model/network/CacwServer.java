package com.hadesky.cacw.model.network;

import com.hadesky.cacw.bean.TaskBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

}
