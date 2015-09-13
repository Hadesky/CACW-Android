package com.hadesky.cacw.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hadesky.cacw.ui.LoginActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * session管理器
 * Created by 45517 on 2015/8/20.
 */
public class SessionManagement {
    private SharedPreferences mPref;
    private WeakReference<Context> contextWeakReference;
    private final static int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "CACWPref";
    private static final String IS_LOGIN = "IsLogIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_AVATAR = "avatar";//头像

    public SessionManagement(Context context) {
        contextWeakReference = new WeakReference<>(context);
        mPref = contextWeakReference.get().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public void createLoginSession(String name, String email) {
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);

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


        // Staring Login Activity
        contextWeakReference.get().startActivity(i);
    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(KEY_NAME, mPref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, mPref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    public boolean isLoggedIn() {
        return mPref.getBoolean(IS_LOGIN, false);
    }
}
