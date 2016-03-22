package com.hadesky.cacw.database;

import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;

/**
 *
 * Created by 45517 on 2015/11/13.
 */
public class UserInfoDAO {

    public UserBean getUserInfo(long userId) {
        DatabaseManager manager = DatabaseManager.getInstance(MyApp.getAppContext());
        DatabaseManager.UserCursor userCursor = manager.queryUser(userId);

        userCursor.moveToFirst();
        UserBean bean = userCursor.getUserBean();
        userCursor.close();
        return bean;
    }


}
