package com.hadesky.cacw.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.SessionManagement;
import com.hadesky.cacw.test.testData;
import com.hadesky.cacw.ui.activity.MainActivity;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;
import com.hadesky.cacw.util.LogUtils;

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
    private AnimProgressDialog progressDialog;
    //Context
    private Context mContext;
    //Session
    private SessionManagement mSession;
    //Volley请求队列
    private RequestQueue mRequestQueue;

    public LoginTask(AnimProgressDialog progressDialog, Context context,SessionManagement session) {
        this.progressDialog = progressDialog;
        mContext = context;
        mSession = session;
        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    @Override
    protected Integer doInBackground(String... params) {
        try {

            // TODO: 2015/10/29 0029 生成模拟数据,后期删除
            testData.createTestData();

            return login(params);
        } catch (Exception e) {
            return ERROR_OTHER;
        }
    }

    private Integer login(String[] params) {
        int[] a = {1, 2};
        mRequestQueue.add(new StringRequest(Request.Method.POST, params[0], new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d("ResponseTAG", "" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("ERROR Response", "" + error.getMessage());
            }
        }));
        return SUCCESS_NORMAL;
    }

//    private Integer login(String[] parms) throws IOException, JSONException,HttpRetryException {
//        String result = "";
//        InputStream is = null;
//        HttpURLConnection connection;
//        try {
//
//            URL url = new URL(parms[0]);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//            connection.setUseCaches(false);
//
//            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            connection.addRequestProperty("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
//            connection.addRequestProperty("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.5");
//            connection.addRequestProperty("Content-Language", "UTF-8");
//            connection.addRequestProperty("Accept-Encoding", "gzip, deflate");
//            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
//            connection.addRequestProperty("Pragma", "no-cache");
//            connection.setRequestProperty("Command", "login");
//            connection.setReadTimeout(10000);
//            connection.setConnectTimeout(15000);
//            //start
//            connection.connect();
//            is = connection.getInputStream();
//
//            InputStreamReader isr = new InputStreamReader(is);
//
//            BufferedReader br = new BufferedReader(isr);
//
//            String inputLine;
//            while ((inputLine = br.readLine()) != null) {
//                result += inputLine + "\n";
//            }
//
//            LogUtils.d("Result TAG", result + "");
//
//            int response = connection.getResponseCode();
//            Log.d("Login Tag", "The response is " + response);
//            return SUCCESS_NORMAL;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ERROR_OTHER;
//        }
//    }

    @Override
    protected void onPreExecute() {
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


        UserBean bean = getProfileBean();
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
     * @return UserBean
     */
    public UserBean getProfileBean() {
        UserBean bean = new UserBean();

        return bean;
    }
}
