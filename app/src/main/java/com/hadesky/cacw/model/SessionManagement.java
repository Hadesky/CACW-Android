package com.hadesky.cacw.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * session管理器
 * Created by 45517 on 2015/8/20.
 */
public class SessionManagement
{
    private static final String KEY_PHONE = "phoneNumber";
    private SharedPreferences mPref;
    private final static int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "CACWPref";


    public SessionManagement(Context context)
    {
        mPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public void clear()
    {

        File file = new File(MyApp.getAppContext().getFilesDir(), "user");
        file.delete();
        MyApp.setCurrentUser(null);
        saveSeesion(null);
    }


    public boolean isLogin()
    {
        return getSession() != null;
    }

    public UserBean getUser()
    {
        FileInputStream inputStream = null;
        ObjectInputStream ois = null;
        UserBean u=null;
        try
        {
            File file = new File(MyApp.getAppContext().getFilesDir(), "user");
            inputStream = new FileInputStream(file);
            ois = new ObjectInputStream(inputStream);
            u = (UserBean) ois.readObject();
            return u;
        }
        catch (Exception e)
        {
            Log.e("tag", e.getMessage());
        }
        finally
        {
            try
            {
                if (inputStream != null)
                    inputStream.close();
                if (ois != null)
                    ois.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        return null;
    }


    public void saveUser(UserBean u)
    {
        FileOutputStream outputStream = null;
        ObjectOutputStream oos = null;
        try
        {
            File file = new File(MyApp.getAppContext().getFilesDir(), "user");
            outputStream = new FileOutputStream(file);
            oos = new ObjectOutputStream(outputStream);
            oos.writeObject(u);
            oos.flush();
            MyApp.setCurrentUser(u);
        }
        catch (Exception e)
        {
            Log.e("tag", e.getMessage());
        }
        finally
        {
            try
            {
                if (outputStream != null)
                    outputStream.close();
                if (oos != null)
                    oos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void saveSeesion(String s)
    {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("ss", s);
        editor.apply();
    }

    public String getSession()
    {
        return mPref.getString("ss", null);
    }

}
