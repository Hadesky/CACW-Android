package com.hadesky.cacw.model.DB;

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
    public static final String Column_IsMe = "isMe";


    public static final String Column_Other = "other";
    public static final String Column_TeamId = "teamId";

    public static final String Column_Id = "id";
    public static final String Column_NickName = "NickName";
    public static final String Column_AvatarUrl = "avatarUrl";


    private UserBean mUser;

    private DatabaseManager(Context context)
    {
        mUser = MyApp.getCurrentUser();
        mHelper = new DatabaseHelper(context,"user_"+mUser.getId());
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
        instance = null;
    }

    /**
     * 根据ID查找消息
     *
     * @param id       用户 id
     * @param pageSise 每页大小
     * @param PageNum  页码 从 1 开始
     * @return 消息列表
     */
    public List<MessageBean> queryMessageByUser(int id, int pageSise, int PageNum)
    {

        List<MessageBean> list = new ArrayList<>();

        if (pageSise <= 0 || PageNum <= 0)
            return list;

        //先算出有多个条数据
        StringBuilder sql = new StringBuilder("select * from " + Table_Message +
                " where " + Column_Other + " = ?");

        Cursor cursor = db.rawQuery(sql.toString(), new String[]{String.valueOf(id)});

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
        cursor = db.rawQuery(sql.toString(), new String[]{String.valueOf(id)});
        while (cursor.moveToNext())
        {
            list.add(getMessage(cursor));
        }
        cursor.close();
        return list;
    }


    public MessageBean queryLastMessageByUser(int id)
    {
        String sql = "select * from " + Table_Message + " " +
                "where " + Column_Other+"=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id+""});
        MessageBean mb = null;
        if (cursor.moveToLast())
            mb = getMessage(cursor);
        cursor.close();
        return mb;
    }


    public List<Integer> queryAllUserId()
    {
        String sql = "select * from " + Table_Users;
        Cursor cursor = db.rawQuery(sql, null);

        List<Integer> list = new ArrayList<>();

        while (cursor.moveToNext())
        {
            list.add(cursor.getInt(cursor.getColumnIndex(Column_Id)));
        }
        cursor.close();
        return list;
    }

    public void saveMessage(MessageBean bean)
    {
        saveOrUpdateUser(bean.getOther());
        ContentValues cv = new ContentValues();
        cv.put(Column_Id,bean.getId());
        cv.put(Column_Other,bean.getOther().getId());
        cv.put(Column_Type, bean.getType());
        cv.put(Column_Content, bean.getContent());
        cv.put(Column_hasRead, bean.isHasRead());
        cv.put(Column_IsMe,bean.isMe());
        cv.put(Column_TeamId,bean.getTeamid());
        db.insert(Table_Message, null, cv);
    }

    public void saveMessage(List<MessageBean> list)
    {

        db.beginTransaction();
        for(MessageBean bean : list)
        {
           saveMessage(bean);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void saveOrUpdateUser(UserBean user)
    {
        Cursor cursor = db.rawQuery("select * from " + Table_Users + " where " + Column_Id + " =? ", new String[]{user.getId()+""});
        if (!cursor.moveToFirst())
        {
            ContentValues cv = new ContentValues();
            cv.put(Column_Id, user.getId());
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
            db.update(Table_Users, cv, Column_Id + "=?", new String[]{user.getId()+""});
        }
        cursor.close();
    }


    //把这个人的对话全部设为已读
    public void setMessageHasRead(int other)
    {
        ContentValues cv = new ContentValues(1);
        cv.put(Column_hasRead, true);
        db.update(Table_Message, cv, Column_Other + " = ?", new String[]{other+""});
    }


    public void setMessageHasRead(List<MessageBean> list)
    {

        db.beginTransaction();
        ContentValues cv = new ContentValues(1);
        for(MessageBean mb : list)
        {
            cv.put(Column_hasRead, true);
            db.update(Table_Message,cv,Column_Other+ "=?", new String[]{mb.getOther().getId()+""});
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
        db.delete(Table_Users, Column_Id + "=?", new String[]{objectId});
    }

    public void deleteUserAndMessage(int id)
    {
        db.delete(Table_Users, Column_Id + "=?", new String[]{String.valueOf(id)});
        db.delete(Table_Message, Column_Other+"=?", new String[]{String.valueOf(id)});
    }


    public UserBean getUserById(int id)
    {
        String sql = "Select * from " + Table_Users + " Where " + Column_Id + " =? ";
        Cursor cursor = db.rawQuery(sql, new String[]{id+""});
        UserBean userBean = null;
        if (cursor.moveToFirst())
            userBean = getUserBean(cursor);
        cursor.close();
        return userBean;

    }


    private UserBean getUserBean(Cursor cursor)
    {

        UserBean userBean = new UserBean();
        int id = cursor.getInt(cursor.getColumnIndex(Column_Id));
        String nickName = cursor.getString(cursor.getColumnIndex(Column_NickName));

        if (!cursor.isNull(cursor.getColumnIndex(Column_AvatarUrl)))
        {
             String url = cursor.getString(cursor.getColumnIndex(Column_AvatarUrl));
            userBean.setAvatarUrl(url);
        }

        userBean.setNickName(nickName);
        userBean.setId(id);
        return userBean;
    }

    private MessageBean getMessage(Cursor cursor)
    {

        MessageBean bean = new MessageBean();
        bean.setOther(getUserById(cursor.getInt(cursor.getColumnIndex(Column_Other))));
        int type = cursor.getInt(cursor.getColumnIndex(Column_Type));
        bean.setType(type);
        String content = cursor.getString(cursor.getColumnIndex(Column_Content));
        bean.setContent(content);
        int read = cursor.getInt(cursor.getColumnIndex(Column_hasRead));
        bean.setHasRead(read == 1);
        bean.setTeamid(cursor.getInt(cursor.getColumnIndex(Column_TeamId)));
        bean.setId(cursor.getInt(cursor.getColumnIndex(Column_Id)));
        bean.setMe(cursor.getInt(cursor.getColumnIndex(Column_IsMe))==1);
        return bean;
    }

}
