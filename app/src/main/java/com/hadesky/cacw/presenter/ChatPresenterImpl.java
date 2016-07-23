package com.hadesky.cacw.presenter;

import com.hadesky.cacw.adapter.ChatAdapter;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.database.DatabaseManager;
import com.hadesky.cacw.ui.view.ChatView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 聊天逻辑
 * Created by dzysg on 2016/7/23 0023.
 */
public class ChatPresenterImpl implements ChatPresenter
{



    ChatView mView;
    UserBean mReceiver;
    UserBean mUSer;
    DatabaseManager mDatabaseManager;
    Map<MessageBean,Integer> mSendingQueue = new HashMap<>(10);
    ChatAdapter mAdapter;


    int page = 0;
    int pageSize = 20;

    public ChatPresenterImpl(ChatView view,UserBean receiver,ChatAdapter adapter)
    {
        mAdapter = adapter;
        mView = view;
        mReceiver = receiver;
        mUSer = MyApp.getCurrentUser();
        mDatabaseManager = DatabaseManager.getInstance(MyApp.getAppContext());

        // TODO: 2016/7/23 0023 信息量大会有性能问题
        mDatabaseManager.setMessageHasRead(mReceiver.getObjectId());
    }

    @Override
    public void loadChatMessage()
    {

        page = 0;
        List<MessageBean> list =  mDatabaseManager.queryMessageByUser(mReceiver.getObjectId(),pageSize,page);
        mView.showChatList(list);
    }

    @Override
    public void send(String text)
    {
        final MessageBean mb = new MessageBean();
        mb.setSender(mUSer);
        mb.setReceiver(mReceiver);
        mb.setType(MessageBean.TYPE_USER_TO_USER);
        mb.setMsg(text);

        mAdapter.addNewChat(mb);

        mb.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e)
            {
                if (e==null)
                {
                    mDatabaseManager.saveMessage(mb);
                    mAdapter.onSucceed(mb);
                }else
                {
                    mAdapter.onFail(mb);
                }
            }
        });

    }

    @Override
    public void loadMore()
    {

    }

    @Override
    public void onDestroy()
    {

    }
}
