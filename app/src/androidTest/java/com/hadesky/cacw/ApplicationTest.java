package com.hadesky.cacw;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.util.StringUtils;

import cn.bmob.v3.BmobQuery;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        assertEquals(StringUtils.isEmail("qweqqc.@om"), true);
    }
}