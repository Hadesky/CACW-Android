package com.hadesky.cacw.task;

import android.content.Context;
import android.os.AsyncTask;

import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.database.UserInfoDAO;
import com.hadesky.cacw.util.NetworkUtils;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;

/**
 * Created by 45517 on 2015/11/13.
 */
public class GetUserInfoTask extends AsyncTask<Long,Void,UserBean> {

    public static final String TAG = "GetUserInfoTask";

    private UserInfoDAO dao;

    private UserBean userBean;

    private AnimProgressDialog dialog;

    private Callback callback;

    public GetUserInfoTask(Context context, Callback callback) {
        dao = new UserInfoDAO();
        dialog = new AnimProgressDialog(context);
        this.callback = callback;
    }

    @Override
    protected void onPostExecute(UserBean userBean) {
        if (userBean != null) {
            callback.onSuccess(userBean);
        } else {
            callback.onFailure("获取出错");
        }
        dialog.cancel();
    }


    @Override
    protected void onPreExecute() {
        dialog.show();
    }

    @Override
    protected UserBean doInBackground(Long... params) {
        UserBean bean = getUserInfoByCache(params[0]);
        if (NetworkUtils.isNetworkConnected(MyApp.getAppContext())) {
//            bean = getUserInfoByNetwork(params[0]);
        }
        return bean;
    }

    private UserBean getUserInfoByCache(Long user_id) {
        return dao.getUserInfo(user_id);
    }

    public UserBean getUserInfoByNetwork(long user_id) {
        return null;
    }

    public interface Callback {
        void onSuccess(UserBean bean);

        void onFailure(String msg);
    }
}
