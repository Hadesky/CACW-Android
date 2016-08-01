package com.hadesky.cacw.presenter;

import com.hadesky.cacw.JPush.JPushSender;
import com.hadesky.cacw.adapter.ChatAdapter;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.database.DatabaseManager;
import com.hadesky.cacw.ui.view.ChatView;
import com.hadesky.cacw.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 聊天逻辑
 * Created by dzysg on 2016/7/23 0023.
 */
public class ChatPresenterImpl implements ChatPresenter
{
    private ChatView mView;
    private UserBean mReceiver;
    private UserBean mUSer;
    private DatabaseManager mDatabaseManager;
    private ChatAdapter mAdapter;

    private boolean mNoMore;
    private int page = 1;
    private int pageSize = 20;
    private boolean mLoading = false; //代表当前正在获取聊天记录
    private boolean haveNewMsg = false;

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
    public void loadNewMsg()//从网络获取最新消息
    {

        if (mLoading)//如果上一次加载还没有完成，先等上一次加载完，并记录haveNewMsg为true，当加载完成后再进行一次loadNewMsg
        {
            haveNewMsg = true;
            return;
        }
        mLoading = true;
        BmobQuery<MessageBean> query = new BmobQuery<>();
        query.addWhereEqualTo("mReceiver", new BmobPointer(mUSer));
        query.addWhereEqualTo("mSender", new BmobPointer(mReceiver));
        query.findObjects(new FindListener<MessageBean>()
        {
            @Override
            public void done(List<MessageBean> list, BmobException e)
            {
                if (e == null)
                {
                    if (list.size() > 0)
                    {
                        handleNewChat(list);
                    } else
                        mLoading = false;
                } else
                {
                    mView.showMsg(e.getMessage());
                    mLoading = false;
                }

            }
        });
    }

    private void handleNewChat(final List<MessageBean> mlist)
    {

        for(MessageBean mb : mlist)
        {
            mb.setHasRead(true);
        }

        //删除后台数据
        BmobBatch b = new BmobBatch();
        List<BmobObject> olist = new ArrayList<>();
        olist.addAll(mlist);
        b.deleteBatch(olist);
        b.doBatch(new QueryListListener<BatchResult>()
        {
            @Override
            public void done(List<BatchResult> list, BmobException e)
            {
                if (e == null)
                {
                    mDatabaseManager.saveMessage(mlist);//保存到本地数据库
                    loadChatMessage();//重新从数据库获取一次
                } else
                {
                    mView.showMsg(e.getMessage());
                }
                mLoading = false;
                if (haveNewMsg)
                {
                    loadNewMsg();
                    haveNewMsg = false;
                }
            }
        });
    }


    @Override
    public void send(final String text)
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
                    sendPush(text);
                } else
                {
                    mAdapter.onFail(mb);
                }
            }
        });
    }


    //发送私信推送
    private void sendPush(String text)
    {

        String title;
        String content;

        title = mUSer.getNickName() + " 给你发了一条消息";
        content = StringUtils.makeSendMsd(mUSer, text);
        JPushSender sender = new JPushSender.SenderBuilder().addAlias(mReceiver.getObjectId()).Message(title, content).build();
        MyApp.getJPushManager().sendMsg(sender, null);
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

        query.findObjects(new FindListener<TeamMember>()
        {
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

    //通知对方请求被接受
    private void sendAcceptPush(MessageBean bean)
    {
        //如果这个消息是团队拉我，通知团队管理员
        if (bean.getType().equals(MessageBean.TYPE_TEAM_TO_USER))
        {
            String title = "团队消息";
            String content =  "用户 "+mUSer.getNickName()+ "接受了你的团队邀请";
            JPushSender sender = new JPushSender.SenderBuilder().addAlias(bean.getSender().getObjectId()).Message(title, content).build();
            MyApp.getJPushManager().sendMsg(sender, null);

        } else //否则就是别人要进我的团队，通知申请者
        {
            String teamname = StringUtils.getTeamNameByMessageBean(bean);
            String title = "团队消息";
            String content =  "你的申请已被接受，成功加入团队 "+teamname;
            JPushSender sender = new JPushSender.SenderBuilder().addAlias(bean.getSender().getObjectId()).Message(title, content).build();
            MyApp.getJPushManager().sendMsg(sender, null);
        }
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
                    sendAcceptPush(bean);
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
