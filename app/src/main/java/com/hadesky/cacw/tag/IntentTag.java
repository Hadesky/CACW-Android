package com.hadesky.cacw.tag;

/**
 *
 * Created by 45517 on 2015/10/28.
 */
public final class IntentTag {
    public static final String TAG_TEAM_BEAN = "team_bean";
    public static final String TAG_USER_BEAN = "user_bean";
    public static final String TAG_PROJECT_BEAN = "project_bean";
    public static final String TAG_Task_MEMBER = "task_member";
    public static final String TAG_TEAM_MEMBER = "team_member";
    public static final String TAG_USER_BEAN_LIST = "team_member";

    private IntentTag() {
        throw new AssertionError();
    }
    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_AVATAR = "avatar";
    public static final String TAG_PROJECT_ID = "project_id";
    public static final String TAG_PROJECT_NAME = "project_name";
    public static final String TAG_TASK_ID = "task_id";
    public static final String TAG_TITLE = "title";
    public static final String TAG_TEAM_ID = "team_id";
    public static final String TAG_TASK_STATUS = "task_status";


    public static final String ACTION_MSG_RECEIVE = "com.hadesky.cacw.intent.MSG";


    public static final String TAG_PUSH_TASK = "tk";
    public static final String TAG_PUSH_MSG = "ms";
    public static final String TAG_PUSH_TEAM = "tm";


}
