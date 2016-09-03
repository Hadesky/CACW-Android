package com.hadesky.cacw.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.util.StringUtils;

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
    public static final String Column_Sender = "Sender";
    public static final String Column_Receiver = "Receiver";
    public static final String Column_Content = "content";
    public static final String Column_Type = "type";
    public static final String Column_hasRead = "hasRead";


    public static final String Column_OId = "ObjectId";
    public static final String Column_NickName = "NickName";
    public static final String Column_AvatarUrl = "avatarUrl";


    private int mUser;

    private DatabaseManager(Context context)
    {
        mUser = MyApp.getCurrentUser().getId();
        mHelper = new DatabaseHelper(context,"user_"+mUser);
        db = mHelper.getWritableDatabase();
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

    public static void closeDb()
    {
        if (instance==null)
            return;
        instance.db.close();
    }


    /**
     * 根据ID查找消息
     *
     * @param id       用户Objectid
     * @param pageSise 每页大小
     * @param PageNum  页码 从 1 开始
     * @return 消息列表
     */
    public List<MessageBean> queryMessageByUser(String id, int pageSise, int PageNum)
    {

        List<MessageBean> list = new ArrayList<>();

        if (pageSise <= 0 || PageNum <= 0)
            return list;


        //先算出有多个条数据
        StringBuilder sql = new StringBuilder("select * from " + Table_Message + " " +
                "where " + Column_Sender + " = ? or " + Column_Receiver + "= ? ");// limit " + pageSise + " offset " + offset;

        Cursor cursor = db.rawQuery(sql.toString(), new String[]{id, id});

        int count = cursor.getCount();
        cursor.close();
        if (count == 0)
            return list;

        //算出 offset
        int offset = count - pageSise * PageNum;
        int limit = pageSise;


        if (offset < 0)
        {
            if (-offset < pageSise)
            {
                offset = 0;
                limit = pageSise +offset;
            }
            else
                return list;
        }


        sql.append("limit ").append(limit).append("  offset ").append(offset);
        cursor = db.rawQuery(sql.toString(), new String[]{id, id});

        while (cursor.moveToNext())
        {
            list.add(getMessage(cursor));
        }

        cursor.close();
        return list;
    }

    public MessageBean queryLastMessageByUser(String id)
    {

        String sql = "select * from " + Table_Message + " " +
                "where " + Column_Sender + " = ? or " + Column_Receiver + "= ? ";

        Cursor cursor = db.rawQuery(sql, new String[]{id, id});

        MessageBean mb = null;

        if (cursor.moveToLast())
            mb = getMessage(cursor);
        cursor.close();
        return mb;
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

    public void saveMessage(MessageBean bean)
    {


        if (bean.getSender().getId()==mUser)
            saveOrUpdateUser(bean.getReceiver());
        else
            saveOrUpdateUser(bean.getSender());

        ContentValues cv = new ContentValues();
//        cv.put(Column_Sender, bean.getSender().getObjectId());
//        cv.put(Column_Receiver, bean.getReceiver().getObjectId());
        cv.put(Column_Type, bean.getType());
        cv.put(Column_Content, bean.getMsg());
        cv.put(Column_hasRead, bean.getHasRead());
        db.insert(Table_Message, null, cv);

    }

    public void saveMessage(List<MessageBean> list)
    {

        db.beginTransaction();
        for(MessageBean bean : list)
        {
            if (bean.getSender().getId()==mUser)
                saveOrUpdateUser(bean.getReceiver());
            else
                saveOrUpdateUser(bean.getSender());

            ContentValues cv = new ContentValues();
            cv.put(Column_Sender, bean.getSender().getId());
            cv.put(Column_Receiver, bean.getReceiver().getId());
            cv.put(Column_Type, bean.getType());
            cv.put(Column_Content, bean.getMsg());
            cv.put(Column_hasRead, bean.getHasRead());
            db.insert(Table_Message, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void saveOrUpdateUser(UserBean user)
    {
        Cursor cursor = db.rawQuery("select * from " + Table_Users + " where " + Column_OId + " =? ", new String[]{user.getId()+""});
        if (!cursor.moveToFirst())
        {
            ContentValues cv = new ContentValues();
            cv.put(Column_OId, user.getId());
            cv.put(Column_NickName, user.getNickName());
            if (user.getAvatarUrl() != null)
                cv.put(Column_AvatarUrl, user.getAvatarUrl());
            db.insert(Table_Users, null, cv);
        } else
        {
            ContentValues cv = new ContentValues();
            if (user.getAvatarUrl() != null)
                cv.put(Column_AvatarUrl, user.getAvatarUrl());
            cv.put(Column_NickName, user.getNickName());
            db.update(Table_Users, cv, Column_OId + "=?", new String[]{user.getId()+""});
        }
        cursor.close();
    }


    public void setMessageHasRead(String oid)
    {
        ContentValues cv = new ContentValues(1);
        cv.put(Column_hasRead, true);
        db.update(Table_Message, cv, Column_Sender + " = ? or " + Column_Receiver + " =? ", new String[]{oid, oid});
    }


    public void setMessageHasRead(List<MessageBean> list)
    {
        db.beginTransaction();

        ContentValues cv = new ContentValues(1);
        String column = "";
        for(MessageBean mb : list)
        {
            if (MyApp.isCurrentUser(mb.getReceiver()))
                column = Column_Receiver;
            else
                column = Column_Sender;

            cv.put(Column_hasRead, true);
            db.update(Table_Message, cv, column + "=?", new String[]{column});
        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    public void setMessageHasRead(MessageBean mb)
    {
        ContentValues cv = new ContentValues(1);
        String column = "";

        if (MyApp.isCurrentUser(mb.getReceiver()))
            column = Column_Receiver;
        else
            column = Column_Sender;

        cv.put(Column_hasRead, true);
        db.update(Table_Message, cv, column + "=?", new String[]{column});


    }

    public void deleteInviteMessage(MessageBean bean)
    {
        db.delete(Table_Message,Column_Content+" like ?",new String[]{StringUtils.getTeamIdByMessageBean(bean)+"$%"});
    }


    public void deleteUser(String objectId)
    {
        db.delete(Table_Users, Column_OId + "=?", new String[]{objectId});
    }

    public void deleteUserAndMessage(String objectId)
    {
        db.delete(Table_Users, Column_OId + "=?", new String[]{objectId});
        db.delete(Table_Message, Column_Sender + "=? or " + Column_Receiver + "=?", new String[]{objectId, objectId});
    }


    public UserBean getUserById(String id)
    {
        String sql = "Select * from " + Table_Users + " Where " + Column_OId + " =? ";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        UserBean userBean = null;
        if (cursor.moveToFirst())
            userBean = getUserBean(cursor);
        cursor.close();
        return userBean;

    }


    private UserBean getUserBean(Cursor cursor)
    {

        UserBean userBean = new UserBean();
        String oid = cursor.getString(cursor.getColumnIndex(Column_OId));
        String nickName = cursor.getString(cursor.getColumnIndex(Column_NickName));

        if (!cursor.isNull(cursor.getColumnIndex(Column_AvatarUrl)))
        {
            String url = cursor.getString(cursor.getColumnIndex(Column_AvatarUrl));
            //userBean.setAvatarUrl(url);
        }

        userBean.setNickName(nickName);

        return userBean;
    }

    private MessageBean getMessage(Cursor cursor)
    {
        // TODO: 2016/9/1 0001
//        MessageBean bean = new MessageBean();
//
//        String sender = cursor.getString(cursor.getColumnIndex(Column_Sender));
//        if (sender.equals(mUser.getId()))
//        {
//            bean.setSender(mUser);
//            String to = cursor.getString(cursor.getColumnIndex(Column_Receiver));
//            bean.setReceiver(getUserById(to));
//        } else
//        {
//            bean.setReceiver(mUser);
//            bean.setSender(getUserById(sender));
//        }
//
//        int type = cursor.getInt(cursor.getColumnIndex(Column_Type));
//        bean.setType((byte) type);
//        String content = cursor.getString(cursor.getColumnIndex(Column_Content));
//        bean.setMsg(content);
//        int read = cursor.getInt(cursor.getColumnIndex(Column_hasRead));
//        bean.setHasRead(read == 1);

        return null;
    }

}
