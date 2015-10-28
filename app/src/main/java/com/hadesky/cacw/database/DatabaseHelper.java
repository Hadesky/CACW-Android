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
                "date TEXT," +
                "time TEXT," +
                "location TEXT" +
                ")");
        //创建表格team
        db.execSQL("CREATE TABLE team(" +
                "team_id INT PRIMARY KEY NOT NULL," +
                "team_name TEXT NOT NULL" +
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
                "project_id INT PRIMARY KEY NOT NULL," +
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
        //创建表格project_task
        db.execSQL("CREATE TABLE project_task (" +
                "project_id INT NOT NULL," +
                "task_id INT NOT NULL," +
                "foreign key (project_id) references project(project_id) on delete cascade on update cascade," +
                "foreign key (task_id) references task(task_id) on delete cascade on update cascade," +
                "PRIMARY KEY(project_id,task_id)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
