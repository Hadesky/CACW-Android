package com.hadesky.cacw.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;

import com.hadesky.cacw.bean.ProfileBean;
import com.hadesky.cacw.database.DataBaseManager;
import com.hadesky.cacw.ui.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;

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
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_AVATAR = "avatar";//头像

    private Context mContext;

    public SessionManagement(Context context) {
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

    public void createLoginSession(ProfileBean bean) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, bean.getUserName());
        editor.putString(KEY_EMAIL, bean.getUserEmail());
        editor.putString(KEY_PHONE, bean.getUserPhoneNumber());

        //放入头像
        Bitmap bitmap = bean.getUserAvatar();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray=byteArrayOutputStream.toByteArray();
        String imageString= Base64.encodeToString(byteArray, Base64.DEFAULT);
        editor.putString(KEY_AVATAR, imageString);

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
        DataBaseManager manager = DataBaseManager.getInstance(mContext);
        manager.cleanAllData();

        // Staring Login Activity
        contextWeakReference.get().startActivity(i);
    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(KEY_NAME, mPref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, mPref.getString(KEY_EMAIL, null));

        user.put(KEY_PHONE, mPref.getString(KEY_PHONE, null));

        user.put(KEY_AVATAR, mPref.getString(KEY_AVATAR, null));

        // return user
        return user;
    }

    public boolean isLoggedIn() {
        return mPref.getBoolean(IS_LOGIN, false);
    }
}
