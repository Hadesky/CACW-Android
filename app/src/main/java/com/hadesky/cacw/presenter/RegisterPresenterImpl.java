package com.hadesky.cacw.presenter;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.View.RegisterView;
import com.hadesky.cacw.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 45517 on 2015/11/22.
 */
public class RegisterPresenterImpl implements RegisterPresenter {
    public static final String TAG = RegisterPresenterImpl.class.getSimpleName();

    private RegisterView registerView;


    public RegisterPresenterImpl(RegisterView view) {
        registerView = view;
    }

    @Override
    public void getAuthCode(String email) {
        registerView.showProgressDialog();

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
                        registerView.hideProgressDialog();
                        Log.d(TAG, response.toString());
                        registerView.disableGetCodeBt();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        registerView.hideProgressDialog();
                        registerView.showMsg(error.toString());
                        LogUtils.d(TAG, error.toString());
                        registerView.disableGetCodeBt();
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
        registerView.getMyApp().addToRequestQueue(jsObjRequest, TAG);
    }

    @Override
    public void register(String email, String pw, String authCode) {

    }

    @Override
    public void cancelRequest() {
        registerView.getMyApp().cancelPendingRequest(TAG);
    }
}
