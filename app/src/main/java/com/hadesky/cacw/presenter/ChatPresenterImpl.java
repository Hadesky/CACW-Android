package com.hadesky.cacw.presenter;

import com.hadesky.cacw.adapter.ChatAdapter;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.database.DatabaseManager;
import com.hadesky.cacw.ui.view.ChatView;
import com.hadesky.cacw.util.StringUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
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
    ChatAdapter mAdapter;

    boolean mNoMore;
    int page = 1;
    int pageSize = 20;

    public ChatPresenterImpl(ChatView view, UserBean receiver, ChatAdapter adapter)
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
        page = 1;
        mNoMore = false;
        List<MessageBean> list = mDatabaseManager.queryMessageByUser(mReceiver.getObjectId(), pageSize, page);
        mView.showChatList(list);
        if (list.size() == 0)
            mNoMore = true;
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

        mb.save(new SaveListener<String>()
        {
            @Override
            public void done(String s, BmobException e)
            {
                if (e == null)
                {
                    mDatabaseManager.saveMessage(mb);
                    mAdapter.onSucceed(mb);
                } else
                {
                    mAdapter.onFail(mb);
                }
            }
        });

    }

    @Override
    public void loadMore()
    {
        if (mNoMore)
            return;

        page++;
        List<MessageBean> list = mDatabaseManager.queryMessageByUser(mReceiver.getObjectId(), pageSize, page);
        if (list == null || list.size() == 0)
            mNoMore = true;
        else
            mAdapter.addDatasToTop(list);
    }

    @Override
    public void onDestroy()
    {
        mView = null;
        mAdapter = null;
    }

    @Override
    public void AcceptJoinTeam(final MessageBean bean)
    {


        //先判断当前是否已经是成员
        TeamBean b = new TeamBean();
        b.setObjectId(StringUtils.getTeamIdByMessageBean(bean));

        UserBean ub;

        //如果这个消息是团队拉我
        if (bean.getType().equals(MessageBean.TYPE_TEAM_TO_USER))
            ub = mUSer;
        else //否则就是别人要进我的团队
            ub = bean.getSender();

        BmobQuery<TeamMember> query = new BmobQuery<>();
        query.addWhereEqualTo("mUser", new BmobPointer(ub));
        query.addWhereEqualTo("mTeam", new BmobPointer(b));

        query.findObjects(new FindListener<TeamMember>() {
            @Override
            public void done(List<TeamMember> list, BmobException e)
            {
                if (e == null)
                {
                    if (list.size() == 0)
                        addToTeam(bean);
                    else
                    {
                        mView.showMsg("已经是该团队成员");
                        mAdapter.delete(bean);
                        mDatabaseManager.deleteInviteMessage(bean);
                    }

                } else
                {
                    mView.showMsg(e.getMessage());
                }
            }
        });

    }


    private void addToTeam(final MessageBean bean)
    {

        UserBean ub;
        //如果这个消息是团队拉我
        if (bean.getType().equals(MessageBean.TYPE_TEAM_TO_USER))
            ub = mUSer;
        else //否则就是别人要进我的团队
            ub = bean.getSender();

        TeamBean t = new TeamBean();
        t.setObjectId(StringUtils.getTeamIdByMessageBean(bean));
        TeamMember tm = new TeamMember();
        tm.setTeam(t);
        tm.setUser(ub);
        tm.save(new SaveListener<String>()
        {
            @Override
            public void done(String s, BmobException e)
            {
                if (e == null)
                {
                    mAdapter.delete(bean);
                    mDatabaseManager.deleteInviteMessage(bean);
                    mView.showMsg("加入成功");
                } else
                {
                    mView.showMsg(e.getMessage());
                }
            }
        });
    }

    @Override
    public void rejectJoinTeam(MessageBean bean)
    {
        mAdapter.delete(bean);
        mDatabaseManager.deleteInviteMessage(bean);
    }


    @Override
    public void deleteChat()
    {
        if (mDatabaseManager != null)
        {
            mDatabaseManager.deleteUserAndMessage(mReceiver.getObjectId());
            mView.finish();
        }
    }
}
