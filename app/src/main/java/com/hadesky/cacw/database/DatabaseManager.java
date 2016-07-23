package com.hadesky.cacw.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;

import java.util.ArrayList;
import java.util.List;

/**
 * 单例模式
 * 用于操作数据库，写明了基本的数据库操作
 * Created by 45517 on 2015/10/28.
 */
public class DatabaseManager
{


    private DatabaseHelper mHelper;
    private SQLiteDatabase db;

    private static DatabaseManager instance;


    public static final String Table_Message = "Message";
    public static final String Table_Users = "Users";

    public static final String Column_From = "from";
    public static final String Column_To = "to";
    public static final String Column_Content = "content";
    public static final String Column_Type = "type";
    public static final String Column_hasRead = "hasRead";


    public static final String Column_OId = "ObjectId";
    public static final String Column_NickName = "NickName";
    public static final String Column_AvatarUrl = "avatarUrl";


    private UserBean mUser;

    private DatabaseManager(Context context)
    {
        mHelper = new DatabaseHelper(context);
        db = mHelper.getWritableDatabase();
        mUser = MyApp.getCurrentUser();
    }

    public static synchronized DatabaseManager getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new DatabaseManager(context);
        } else if (!instance.db.isOpen())
            instance.db = instance.mHelper.getWritableDatabase();
        return instance;
    }


    public List<MessageBean> queryMessageByUser(String id, int pageSise, int PageNum)
    {

        int offset = PageNum * pageSise;
        String sql = "select * from " + Table_Message + " " +
                "where from = ? or  to= ?  limit " + pageSise + " offset " + offset;

        Cursor cursor = db.rawQuery(sql, new String[]{id, id});

        List<MessageBean> list = new ArrayList<>();

        while (cursor.moveToNext())
        {
            list.add(getMessage(cursor));
        }

        cursor.close();
        return list;
    }


    public List<UserBean> queryAllUsers()
    {
        String sql = "select * from " + Table_Users;
        Cursor cursor = db.rawQuery(sql, null);

        List<UserBean> list = new ArrayList<>();

        while (cursor.moveToNext())
        {
            UserBean u = getUserBean(cursor);
            if (u != null)
            {
                list.add(u);
            }
        }
        cursor.close();
        return list;
    }

    public void saveMessage(List<MessageBean> list)
    {
        for(MessageBean bean : list)
        {

            if (bean.getSender().getObjectId().equals(mUser.getObjectId()))
                saveUser(bean.getReceiver());
            else
                saveUser(bean.getSender());

            ContentValues cv = new ContentValues();
            cv.put(Column_From, bean.getSender().getObjectId());
            cv.put(Column_To, bean.getReceiver().getObjectId());
            cv.put(Column_Type, bean.getType());
            cv.put(Column_Content, bean.getMsg());
            cv.put(Column_hasRead, bean.getHasRead());
            db.insert(Table_Message, null, cv);
        }
    }

    public void saveUser(UserBean user)
    {
        Cursor cursor = db.rawQuery("select * from " + Table_Users + " where " + Column_OId + " =? ", new String[]{user.getObjectId()});
        if (!cursor.moveToFirst())
        {
            ContentValues cv = new ContentValues();
            cv.put(Column_OId, user.getObjectId());
            cv.put(Column_NickName, user.getNickName());
            if (user.getAvatarUrl() != null)
                cv.put(Column_AvatarUrl, user.getAvatarUrl());
            db.insert(Table_Users, null, cv);
        }
        cursor.close();
    }

    public void deleteUser(String objectId)
    {
        db.delete(Table_Users,Column_OId+"=?",new String[]{objectId});
    }

    public void deleteUserAndMessage(String objectId)
    {
        db.delete(Table_Users,Column_OId+"=?",new String[]{objectId});
        db.delete(Table_Message, "from=? or to =?", new String[]{objectId, objectId});
    }




    public UserBean getUserById(String id)
    {
        String sql = "Select * from User Where " + Column_OId + " =? ";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        UserBean userBean = getUserBean(cursor);
        cursor.close();
        return userBean;

    }


    private UserBean getUserBean(Cursor cursor)
    {

        if (cursor.moveToFirst())
            return null;

        UserBean userBean = new UserBean();
        String oid = cursor.getString(cursor.getColumnIndex(Column_OId));
        String nickName = cursor.getString(cursor.getColumnIndex(Column_NickName));

        if (!cursor.isNull(cursor.getColumnIndex(Column_AvatarUrl)))
        {
            String url = cursor.getString(cursor.getColumnIndex(Column_AvatarUrl));
            cursor.getColumnIndex(Column_AvatarUrl);
        }
        userBean.setObjectId(oid);
        userBean.setNickName(nickName);

        return userBean;
    }

    private MessageBean getMessage(Cursor cursor)
    {
        MessageBean bean = new MessageBean();

        String sender = cursor.getString(cursor.getColumnIndex(Column_From));
        if (sender.equals(mUser.getObjectId()))
        {
            bean.setSender(mUser);
            String to = cursor.getString(cursor.getColumnIndex(Column_To));
            bean.setReceiver(getUserById(to));
        } else
        {
            bean.setReceiver(mUser);
            bean.setSender(getUserById(sender));
        }

        int type = cursor.getInt(cursor.getColumnIndex(Column_Type));
        bean.setType((byte) type);
        String content = cursor.getString(cursor.getColumnIndex(Column_Content));
        bean.setMsg(content);
        int read = cursor.getInt(cursor.getColumnIndex(Column_hasRead));
        bean.setHasRead(read == 1);

        return bean;
    }

}
