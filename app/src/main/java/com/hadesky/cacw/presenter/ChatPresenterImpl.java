package com.hadesky.cacw.presenter;

import com.hadesky.cacw.adapter.ChatAdapter;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.MessageRepertory;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.ui.view.ChatView;

import java.util.List;

/**
 * Created by dzysg on 2016/9/9 0009.
 */
public class ChatPresenterImpl implements ChatPresenter
{

    private ChatView mView;
    private UserBean other;
    private UserBean me;
    private ChatAdapter mAdapter;
    private MessageRepertory mMessageRepertory;

    private int page =1;
    private int pageSize = 20;


    public ChatPresenterImpl(ChatView view, ChatAdapter adapter,UserBean other)
    {
        mView = view;
        this.other = other;
        me = MyApp.getCurrentUser();
        mAdapter = adapter;
        mMessageRepertory = new MessageRepertory();
    }

    @Override
    public void loadNewMsg()
    {

    }

    @Override
    public void loadChatMessage()
    {
        mMessageRepertory.getUserMessage(other.getId(),page,pageSize)
                .subscribe(new RxSubscriber<List<MessageBean>>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.showMsg(msg);
                    }
                    @Override
                    public void _onNext(List<MessageBean> list)
                    {
                        mView.showChatList(list);
                    }
                });
    }

    @Override
    public void send(String text)
    {
        final MessageBean bean = new MessageBean();
        bean.setOther(other);
        bean.setContent(text);
        bean.setHasRead(true);
        bean.setMe(true);
        bean.setType(2);
        mAdapter.addNewChat(bean);
        mMessageRepertory.sendMessage(bean)
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onError(String msg)
                    {
                        mView.showMsg(msg);
                        mAdapter.onFail(bean);
                    }

                    @Override
                    public void _onNext(String s)
                    {
                        mAdapter.onSucceed(bean);
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

    @Override
    public void AcceptJoinTeam(MessageBean bean)
    {

    }

    @Override
    public void rejectJoinTeam(MessageBean bean)
    {

    }

    @Override
    public void deleteChat()
    {

    }
}
