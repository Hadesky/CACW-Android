package com.hadesky.cacw;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.hadesky.cacw.util.StringUtils;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        String[] result = StringUtils.cutInviteOrJoinString("abcdef");
        assertEquals(result[0], "abcdef");
    }
}