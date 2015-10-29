package com.hadesky.cacw.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.UserBean;

/**
 * 单例模式
 * 用于操作数据库，写明了基本的数据库操作
 * Created by 45517 on 2015/10/28.
 */
public class DatabaseManager {
    public static final String DB_NAME = "cacw.sqlite";

    public static final String COLUMN_USER_USER_ID = "user_id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_AVATAR = "avatar";
    public static final String COLUMN_PROJECT_PROJECT_ID = "project_id";
    public static final String COLUMN_PROJECT_PROJECT_NAME = "project_name";
    public static final String COLUMN_TASK_TASK_ID = "task_id";
    public static final String COLUMN_TASK_TITLE = "title";
    public static final String COLUMN_TEAM_TEAM_ID = "team_id";
    public static  final String CLOUMN_TASK_IS_COMPLETE_="is_complete";


    public static final String TABLE_USER = "user";
    public static final String TABLE_PROJECT = "project";
    public static final String TABLE_TASK = "task";
    public static final String TABLE_PROJECT_USER = "project_user";
    public static final String TABLE_TASK_USER = "task_user";
    //public static final String TABLE_PROJECT_TASK = "project_task";

    private DatabaseHelper mHelper;
    private SQLiteDatabase db;

    private static DatabaseManager instance;

    private DatabaseManager(Context context) {
        mHelper = new DatabaseHelper(context);
        db = mHelper.getWritableDatabase();
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    /**
     * 插入一个UserBean
     * @param bean 插入的UserBean，UserId和Username不能为空
     * @return 插入后得到的ID
     */
    public long insertUser(UserBean bean) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_USER_ID, bean.getUserId());
        cv.put(COLUMN_USER_USERNAME, bean.getUsername());
        return db.insert(TABLE_USER, null, cv);
    }


    public long insertProject(ProjectBean bean) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PROJECT_PROJECT_ID, bean.getProjectId());
        cv.put(COLUMN_PROJECT_PROJECT_NAME,bean.getProjectName());
        return db.insert(TABLE_PROJECT, null, cv);
    }

    public long insertTask(TaskBean bean) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TASK_TASK_ID, bean.getTaskId());
        cv.put(COLUMN_TASK_TITLE, bean.getTitle());
        cv.put(CLOUMN_TASK_IS_COMPLETE_,bean.getTaskStatus());
        cv.put(COLUMN_PROJECT_PROJECT_ID,bean.getProjectId());
        return db.insert(TABLE_TASK, null, cv);
    }

    /**
     * 在把用户加进Project前必须先加入User表
     * @param user_id 已经加入User表的UserId
     * @param project_id 已经加入Project表的ProjectID
     */
    public void putUserIntoProject(long user_id, long project_id) {
        Cursor userCursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE user_id = " + String.valueOf(user_id), null);
        Cursor projectCursor = db.rawQuery("SELECT * FROM " + TABLE_PROJECT + " WHERE project_id = " + String.valueOf(project_id), null);
        if (userCursor != null && projectCursor != null) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_PROJECT_PROJECT_ID, project_id);
            cv.put(COLUMN_USER_USER_ID, user_id);
            db.insert(TABLE_PROJECT_USER, null, cv);
        }
    }

    public void putUserIntoTask(long user_id, long task_id) {
        Cursor userCursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE user_id = " + String.valueOf(user_id), null);
        Cursor taskCursor = db.rawQuery("SELECT * FROM " + TABLE_TASK + " WHERE task_id = " + String.valueOf(task_id), null);
        if (userCursor != null && taskCursor != null) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_TASK_TASK_ID, task_id);
            cv.put(COLUMN_USER_USER_ID, user_id);
            db.insert(TABLE_TASK_USER, null, cv);
        }
    }

    public void deleteTask(long task_id)
    {
        db.execSQL("DELETE FORM" + TABLE_TASK + " WHERE task_id= " +
                String.valueOf(task_id));
    }

    public void updateTaskComplete(int task_id,int complete)
    {
        db.execSQL("update " + TABLE_TASK + "set is_complete ="+complete+" where task_id ="+
                String.valueOf(task_id));
    }

    public void deleteUserFromTask(long user_id, long task_id) {
        db.execSQL("DELETE FORM" + TABLE_TASK_USER + " WHERE user_id= " +
                String.valueOf(user_id) + " AND task_id= " + String.valueOf(task_id));
    }

    public void deleteUserFromProject(long user_id,long project_id) {
        db.execSQL("DELETE FROM " + TABLE_PROJECT_USER + " WHERE user_id= " +
                String.valueOf(user_id) + " AND project_id= " + String.valueOf(project_id));
    }


    public UserCursor queryUserFromProject(long project_id) {
        Cursor wrapped = db.rawQuery("SELECT user.* FROM " + TABLE_USER + "," + TABLE_PROJECT_USER +
                " WHERE user.user_id=project_user.user_id AND " + "project_user.project_id= " + String.valueOf(project_id), null);
        return new UserCursor(wrapped);
    }

    public ProjectCursor queryProject(long project_id) {
        Cursor wrapped = db.rawQuery("SELECT * FROM " + TABLE_PROJECT +
                " WHERE project_id= " + String.valueOf(project_id), null);
        return new ProjectCursor(wrapped);
    }

    public UserCursor queryUserFromTask(long task_id) {
        Cursor wrapped = db.rawQuery("SELECT user.* FROM " + TABLE_USER + "," + TABLE_TASK_USER +
                " WHERE user.user_id=task_user.user_id AND " + "task_user.task_id= " + String.valueOf(task_id), null);
        return new UserCursor(wrapped);
    }

    public ProjectCursor queryProjectByUserId(long user_id) {
        Cursor wrapped = db.rawQuery("SELECT project.* FROM " + TABLE_PROJECT + "," + TABLE_PROJECT_USER +
                " WHERE project.project_id=project_user.project_id AND " + "project_user.user_id= " + String.valueOf(user_id), null);
        return new ProjectCursor(wrapped);
    }

    public TaskCursor queryTask(long task_id) {
        Cursor wrapped = db.rawQuery("SELECT * FROM " + TABLE_TASK +
                " WHERE task_id= " + String.valueOf(task_id), null);
        return new TaskCursor(wrapped);
    }

    public TaskCursor queryUncompleteTask()
    {
        Cursor wrapped = db.rawQuery("SELECT * FROM " + TABLE_TASK +
                " WHERE is_complete =?",new String[]{"1"});
        return new TaskCursor(wrapped);
    }

    public void cleanAllData() {
        db.execSQL("DELETE FROM " + TABLE_USER);
        db.execSQL("DELETE FROM " + TABLE_PROJECT);
        db.execSQL("DELETE FROM " + TABLE_TASK);
        db.execSQL("DELETE FROM " + TABLE_PROJECT_USER);
        db.execSQL("DELETE FROM " + TABLE_TASK_USER);
    }



    /**
     * 关闭数据库，退出应用时调用
     */
    public void closeDB() {
        db.close();
    }



    /**
     * 内部类
     * 用于格式化查询结果
     */
    public static class UserCursor extends CursorWrapper {

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public UserCursor(Cursor cursor) {
            super(cursor);
        }

        /**
         *从Cursor提取出UserBean，如果对应行为空，则返回Null
         * @return 从Cursor提取出的UserBean，如果对应行为空，则返回Null
         */
        public UserBean getUserBean() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            UserBean bean = new UserBean();
            long user_id = getLong(getColumnIndex(COLUMN_USER_USER_ID));
            bean.setUserId(user_id);
            String username = getString(getColumnIndex(COLUMN_USER_USERNAME));
            bean.setUsername(username);
            if (isNull(getColumnIndex(COLUMN_USER_AVATAR))) {
                bean.setAvatarResid(R.drawable.default_user_image);
            }
            return bean;
        }
    }

    /**
     * 内部类
     * 用于格式化查询结果
     */
    public static class ProjectCursor extends CursorWrapper {

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public ProjectCursor(Cursor cursor) {
            super(cursor);
        }

        /**
         *从Cursor提取出UserBean，如果对应行为空，则返回Null
         * @return 从Cursor提取出的UserBean，如果对应行为空，则返回Null
         */
        public ProjectBean getProjectBean() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            ProjectBean bean = new ProjectBean();
            long project_id = getLong(getColumnIndex(COLUMN_PROJECT_PROJECT_ID));
            bean.setProjectId(project_id);
            String project_name = getString(getColumnIndex(COLUMN_PROJECT_PROJECT_NAME));
            bean.setTitle(project_name);
            // TODO: 2015/10/28 0028  后期要改！！！
            bean.setAvatarResId(R.drawable.default_user_image);

            return bean;
        }
    }

    /**
     * 内部类
     * 用于格式化查询结果
     */
    public static class TaskCursor extends CursorWrapper {

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public TaskCursor(Cursor cursor) {
            super(cursor);
        }

        /**
         *从Cursor提取出UserBean，如果对应行为空，则返回Null
         * @return 从Cursor提取出的UserBean，如果对应行为空，则返回Null
         */
        public TaskBean getTaskBean() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            TaskBean bean = new TaskBean();
            long task_id = getLong(getColumnIndex(COLUMN_TASK_TASK_ID));
            bean.setTaskID(task_id);
            String title = getString(getColumnIndex(COLUMN_TASK_TITLE));
            bean.setTitle(title);
            return bean;
        }
    }
}
