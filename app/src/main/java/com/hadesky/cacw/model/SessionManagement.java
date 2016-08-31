package com.hadesky.cacw.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * session管理器
 * Created by 45517 on 2015/8/20.
 */
public class SessionManagement {
    private static final String KEY_PHONE = "phoneNumber";
    private SharedPreferences mPref;
    private final static int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "CACWPref";
    private static final String IS_LOGIN = "IsLogIn";


    public SessionManagement(Context context) {

        mPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public String getCurrentUser()
    {
        return mPref.getString("user",null);
    }

    public void setCurrentUser(String u)
    {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("user",u);
        editor.apply();
    }


    public boolean isLogin()
    {
       return  mPref.getBoolean(IS_LOGIN,false);
    }

    public void setLogin(boolean l)
    {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(IS_LOGIN,l);
        editor.apply();
    }

    public void saveSeesion(String s)
    {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("ss",null);
        editor.apply();
    }
    public String getSession()
    {
       return  mPref.getString("ss",null);
    }

}
