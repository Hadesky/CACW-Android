package com.hadesky.cacw.presenter;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.view.RegisterView;
import com.hadesky.cacw.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 *  注册页面
 * Created by 45517 on 2015/11/22.
 */
public class RegisterPresenterImpl implements RegisterPresenter {
    public static final String TAG = RegisterPresenterImpl.class.getSimpleName();

    private RegisterView mView;


    public RegisterPresenterImpl(RegisterView view) {
        mView = view;
    }

    @Override
    public void getAuthCode(String email) {
        mView.showProgressDialog();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, MyApp.getURL(), jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mView.hideProgressDialog();
                        Log.d(TAG, response.toString());
                        mView.disableGetCodeBt();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mView.hideProgressDialog();
                        mView.showMsg(error.toString());
                        LogUtils.d(TAG, error.toString());
                        mView.disableGetCodeBt();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("command", "004");
                return headers;
            }
        };

        LogUtils.d(TAG, jsObjRequest.toString());
        mView.getMyApp().addToRequestQueue(jsObjRequest, TAG);
    }

    @Override
    public void register(String email, String pw, String authCode) {
        UserBean bean = new UserBean();
        //把邮箱当用户名
        bean.setEmail(email);
        bean.setUsername(email);
        bean.setPassword(pw);
        bean.signUp(new SaveListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e)
            {
                mView.hideProgressDialog();
                if (e==null)
                {
                    mView.showMsg("注册成功");
                }else
                {
                    mView.showMsg(e.getMessage());
                }
            }

            @Override
            public void onStart()
            {
                mView.showProgressDialog();
            }
        });
    }

    @Override
    public void cancelRequest() {
        mView.getMyApp().cancelPendingRequest(TAG);
    }
}
