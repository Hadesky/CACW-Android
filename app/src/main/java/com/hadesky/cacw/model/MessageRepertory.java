package com.hadesky.cacw.model;

import com.google.gson.JsonObject;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.DB.DatabaseManager;
import com.hadesky.cacw.model.network.CacwServer;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 消息仓库
 * Created by dzysg on 2016/9/9 0009.
 */
public class MessageRepertory
{
    CacwServer mCacwServer;
    DatabaseManager mDatabaseManager;


    private static class holder
    {
        public static MessageRepertory instance = new MessageRepertory();
    }

    public static MessageRepertory getInstance()
    {
        MessageRepertory m = holder.instance;
        m.initDB();
        return m;
    }

    private MessageRepertory()
    {
        mCacwServer = MyApp.getApiServer();
        mDatabaseManager = DatabaseManager.getInstance(MyApp.getAppContext());
    }

    private void initDB()
    {
        mDatabaseManager=DatabaseManager.getInstance(MyApp.getAppContext());
    }

    public Observable<List<MessageBean>> getMessageList()
    {

        return mCacwServer.getMessages()
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<List<MessageBean>>handleResult())
                .doOnNext(new Action1<List<MessageBean>>() {
            @Override
            public void call(List<MessageBean> msgs)
            {
                //先保存到数据库
                if (msgs.size() > 0)
                    mDatabaseManager.saveMessage(msgs);
            }
        }).flatMap(new Func1<List<MessageBean>, Observable<List<MessageBean>>>()
        {
            @Override
            public Observable<List<MessageBean>> call(List<MessageBean> msgs)
            {

                List<Integer> ids = mDatabaseManager.queryAllUserId();//找到所有私信过的人
                List<MessageBean> mlist = new ArrayList<>();
                for(Integer i:ids)
                {
                    MessageBean t = mDatabaseManager.queryLastMessageByUser(i);
                    if(t!=null)
                    mlist.add(t); //找到每个人最后一条消息
                }
                return Observable.just(mlist);
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<MessageBean>> getUserMessage(final int uid, final int page, final int pagesize)
    {
         return Observable.just(uid)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer)
                    {
                        mDatabaseManager.setMessageHasRead(uid);
                    }
                })
                .flatMap(new Func1<Integer, Observable<List<MessageBean>>>() {
                    @Override
                    public Observable<List<MessageBean>> call(Integer integer)
                    {
                        List<MessageBean> list = mDatabaseManager.queryMessageByUser(integer,pagesize,page);
                        return Observable.just(list);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }



    public Observable<Integer> deleteUserMessage(final int uid)
    {
        return Observable.just(uid)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer)
                    {
                        mDatabaseManager.deleteUserAndMessage(integer);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<String> deleteMessageById(final int id)
    {
       return  Observable.just("")
               .subscribeOn(Schedulers.io())
               .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s)
                    {
                        mDatabaseManager.deleteMessageById(id);
                    }
                });
    }


    public Observable<String> sendMessage(final MessageBean bean)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("receiverId",bean.getOther().getId());
        jsonObject.addProperty("content",bean.getContent());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString());

        return  mCacwServer.sendMessage(body)
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.<String>handleResult())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s)
                    {
                        mDatabaseManager.saveMessage(bean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


}

