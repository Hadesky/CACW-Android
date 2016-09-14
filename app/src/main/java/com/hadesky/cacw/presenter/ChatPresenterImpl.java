package com.hadesky.cacw.presenter;

import com.hadesky.cacw.adapter.ChatAdapter;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.MessageRepertory;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.model.TeamRepertory;
import com.hadesky.cacw.ui.view.ChatView;

import java.util.List;

import rx.Subscription;

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
    private TeamRepertory mTeamRepertory;
    private Subscription mSubscription;
    private int page = 1;
    private int pageSize = 20;


    public ChatPresenterImpl(ChatView view, ChatAdapter adapter, UserBean other)
    {
        mView = view;
        this.other = other;
        me = MyApp.getCurrentUser();
        mAdapter = adapter;
        mMessageRepertory = MessageRepertory.getInstance();
        mTeamRepertory = TeamRepertory.getInstance();
    }

    @Override
    public void loadNewMsg()
    {
        //这个是用于收到推送时更新
    }

    @Override
    public void loadChatMessage()
    {
        mMessageRepertory.getUserMessage(other.getId(), page, pageSize).subscribe(new RxSubscriber<List<MessageBean>>()
        {
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
        mSubscription = mMessageRepertory.sendMessage(bean).subscribe(new RxSubscriber<String>()
        {
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
        if (mSubscription != null)
        {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void AcceptJoinTeam(final MessageBean bean)
    {
        int uid;
        //如果是别人申请加入我的团队，uid为别人，否则是我同意别的团队的邀请，uid为自己
        if (bean.getType() == 1)
            uid = bean.getOther().getId();
        else
            uid = me.getId();

            mSubscription = mTeamRepertory.addTeamMember(bean.getTeamid(),uid).subscribe(new RxSubscriber<String>()
            {
                @Override
                public void _onError(String msg)
                {
                    mView.showMsg(msg);
                }

                @Override
                public void _onNext(String s)
                {
                    mView.showMsg("加入成功");
                    mAdapter.delete(bean);
                }
            });
    }

    @Override
    public void rejectJoinTeam(MessageBean bean)
    {
        mAdapter.delete(bean);
        mMessageRepertory.deleteMessageById(bean.getId()).subscribe();
    }

    @Override
    public void deleteChat()
    {

    }
}
