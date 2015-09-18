package com.hadesky.cacw.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.ProfileBean;
import com.hadesky.cacw.config.SessionManagement;
import com.hadesky.cacw.ui.MainActivity;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 登陆时用到的Task
 * Created by 45517 on 2015/9/9.
 */
public class LoginTask extends AsyncTask <String, Void, Integer>{

    public static final int ERROR_ACCOUNT_NO_EXIST = 1;//用户名不存在
    public static final int ERROR_PASSWORD_WRONG = 2;//密码错误
    public static final int ERROR_NO_RESPONSES = 3;//网络故障
    public static final int ERROR_OTHER = -1;//其他错误
    public static final int SUCCESS_NORMAL = 4;//正常登陆

    //登陆时的进度Dialog
    ProgressDialog progressDialog;
    Context mContext;
    SessionManagement mSession;
    private Object profileBean;

    public LoginTask(ProgressDialog progressDialog, Context context,SessionManagement session) {
        this.progressDialog = progressDialog;
        mContext = context;
        mSession = session;
    }

    @Override
    protected Integer doInBackground(String... parms) {
        try {
            return login(parms);
        } catch (Exception e) {
            return ERROR_OTHER;
        }
    }

    private Integer login(String[] parms) throws IOException, JSONException {
        //TODO 需要修改
        URL url = new URL(parms[0]);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        //start
        connection.connect();
        int response = connection.getResponseCode();
        Log.d("Login Tag", "The response is " + response);
        if (response != 200) {
            return ERROR_NO_RESPONSES;
        }
        return SUCCESS_NORMAL;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("登录中...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Integer result) {
        progressDialog.cancel();
        switch (result) {
            case ERROR_PASSWORD_WRONG:
                Toast.makeText(mContext, "用户名或密码错误，请检查后再试", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_ACCOUNT_NO_EXIST:
                Toast.makeText(mContext, "用户名或密码错误，请检查后再试", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_NO_RESPONSES:
                Toast.makeText(mContext, "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_OTHER:
                Toast.makeText(mContext, "未知错误", Toast.LENGTH_SHORT).show();
                break;
            case SUCCESS_NORMAL:
                onSuccessLogin();
                break;
        }
    }

    /**
     * 在账户密码一切正确正常的时候调用
     */
    private void onSuccessLogin() {
        ProfileBean bean = getProfileBean();
        mSession.createLoginSession(bean);

        Intent intent = new Intent();
        intent.setClass(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        progressDialog.cancel();
        mContext.startActivity(intent);
    }

    /**
     * 生成账户信息,TODO,需要改成从网络获取
     * @return ProfileBean
     */
    public ProfileBean getProfileBean() {
        ProfileBean bean = new ProfileBean();
        bean.setUserName("蚂蚁测试员");
        bean.setUserEmail("abc@mayi.com");
        bean.setUserPhoneNumber("123456");
        Bitmap avatar = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_user_image);
        bean.setUserAvatar(avatar);
        return bean;
    }
}
