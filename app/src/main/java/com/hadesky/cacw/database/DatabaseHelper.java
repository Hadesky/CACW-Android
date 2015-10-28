package com.hadesky.cacw.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;

/**
 * Created by 45517 on 2015/10/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "cacw.sqlite";
    public static final int VERSION = 1;
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_USERID = "user_id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_AVATAR = "avatar";

    public DatabaseHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表格task
                db.execSQL("CREATE TABLE task (" +
                        "task_id INT PRIMARY KEY NOT NULL," +
                        "title TEXT NOT NULL," +
                        "date TEXT NOT NULL," +
                        "time TEXT NOT NULL," +
                        "location TEXT" +
                        ")");
        //创建表格task_user
        db.execSQL("CREATE TABLE task_user(" +
                "task_id INT NOT NULL," +
                "user_id INT NOT NULL," +
                "foreign key (task_id) references task(task_id) on delete cascade on update cascade," +
                "foreign key (user_id) references user(user_id) on delete cascade on update cascade," +
                "PRIMARY KEY(task_id,user_id)" +
                ")");

        //创建表格project
                db.execSQL("CREATE TABLE project (" +
                        "project _id INT PRIMARY KEY NOT NULL," +
                        "project_name TEXT NOT NULL" +
                        ")");
        //创建表格project_user
                db.execSQL("CREATE TABLE project_user (" +
                        "project_id INT NOT NULL," +
                        "user_id INT NOT NULL," +
                        "foreign key (project_id) references project(project_id) on delete cascade on update cascade," +
                        "foreign key (user_id) references user(user_id) on delete cascade on update cascade," +
                        "PRIMARY KEY(project_id,user_id)" +
                        ")");
        //创建表格user
        db.execSQL("CREATE TABLE user (" +
                "user_id INT PRIMARY KEY NOT NULL," +
                "username TEXT NOT NULL," +
                "avatar BLOB," +
                "phone_number TEXT," +
                "email TEXT," +
                "address TEXT," +
                "signature TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertUser(UserBean bean) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_USERID, bean.getUserid());
        cv.put(COLUMN_USER_USERNAME, bean.getUsername());
        return getWritableDatabase().insert(TABLE_USER, null, cv);
    }

    public UserCursor queryUser() {
        Cursor wrapped = getReadableDatabase().query(TABLE_USER, null, null, null, null, null, COLUMN_USER_USERID);
        UserCursor userCursor = new UserCursor(wrapped);
        return userCursor;
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
            long user_id = getLong(getColumnIndex(COLUMN_USER_USERID));
            bean.setUserid(user_id);
            String username = getString(getColumnIndex(COLUMN_USER_USERNAME));
            bean.setUsername(username);
            if (isNull(getColumnIndex(COLUMN_USER_AVATAR))) {
                bean.setAvatarResid(R.drawable.default_user_image);
            }
            return bean;
        }
    }
}
