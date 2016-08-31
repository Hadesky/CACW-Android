package com.hadesky.cacw.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.database.DatabaseManager;
import com.hadesky.cacw.ui.activity.LoginActivity;

import java.lang.ref.WeakReference;

/**
 * session管理器
 * Created by 45517 on 2015/8/20.
 */
public class SessionManagement {
    private static final String KEY_PHONE = "phoneNumber";
    private SharedPreferences mPref;
    private WeakReference<Context> contextWeakReference;
    private final static int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "CACWPref";
    private static final String IS_LOGIN = "IsLogIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    public static final String KEY_AVATAR = "avatar";//头像
    private static final String KEY_USER_ID = "user_id";

    private Context mContext;

    SessionManagement(Context context) {
        contextWeakReference = new WeakReference<>(context);
        mPref = contextWeakReference.get().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mContext = context;
    }

    public void createLoginSession(String name, String email) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);

        editor.apply();
    }

    public void createLoginSession(UserBean bean) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, bean.getUsername());

        //放入头像
        editor.apply();
    }

    public boolean checkLogin() {
        if (!isLoggedIn()) {
            Intent intent = new Intent(contextWeakReference.get(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            contextWeakReference.get().startActivity(intent);
            return false;
        } else {
            return true;
        }
    }

    public void logoutUser() {
        final SharedPreferences.Editor editor = mPref.edit();
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.apply();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(contextWeakReference.get(), LoginActivity.class);
        // Closing all the Activities
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        //退出登录清除数据库所有数据
        DatabaseManager manager = DatabaseManager.getInstance(mContext);
        //manager.cleanAllData();

        // Staring Login Activity
        contextWeakReference.get().startActivity(i);
    }


    public UserBean getUserDetails() {

        UserBean bean = new UserBean();
        bean.setUsername(mPref.getString(KEY_NAME, "蚂蚁"));

        // return user
        return bean;
    }

    public boolean isLoggedIn() {
        return mPref.getBoolean(IS_LOGIN, false);
    }
}
