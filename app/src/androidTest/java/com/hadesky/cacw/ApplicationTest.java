package com.hadesky.cacw;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.hadesky.cacw.bean.UserBean;

import cn.bmob.v3.BmobQuery;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        BmobQuery<UserBean> query = new BmobQuery<>();
    }
}