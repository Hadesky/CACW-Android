package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.database.DatabaseManager;
import com.hadesky.cacw.ui.view.MessageListView;

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
import rx.Subscription;

/**
 * Created by dzysg on 2016/7/23 0023.
 */
public class MessageListPresenterImpl implements MessageListPresenter
{


    MessageListView mView;
    DatabaseManager mDatabaseManager;
    UserBean mUser;
    Subscription mSubscription;
    List<MessageBean> mList;

    public MessageListPresenterImpl(MessageListView view)
    {
        mView = view;
        mDatabaseManager = DatabaseManager.getInstance(MyApp.getAppContext());
        mUser = MyApp.getCurrentUser();
    }


    @Override
    public void onDestroy()
    {
        if (mSubscription != null)
            mSubscription.unsubscribe();
    }

    @Override
    public void LoadMessage()
    {
        mView.showProgress();
        BmobQuery<MessageBean> query = new BmobQuery<>();
        query.addWhereEqualTo("mSender", new BmobPointer(mUser));
        BmobQuery<MessageBean> query2 = new BmobQuery<>();
        query.addWhereEqualTo("mReceiver", new BmobPointer(mUser));
        BmobQuery<MessageBean> queryOr = new BmobQuery<>();
        List<BmobQuery<MessageBean>> list = new ArrayList<>();
        list.add(query);
        list.add(query2);
        queryOr.or(list);

        mSubscription = queryOr.findObjects(new FindListener<MessageBean>()
        {
            @Override
            public void done(List<MessageBean> list, BmobException e)
            {
                if (e != null)
                {
                    mView.hideProgress();
                    mView.showMsg(e.getMessage());
                } else
                {
                    mList = list;
                    deleteFromBmob(list); //删除后台的数据
                }
            }
        });

    }

    private void deleteFromBmob(List<MessageBean> list)
    {
        BmobBatch batch = new BmobBatch();
        batch.deleteBatch(new ArrayList<BmobObject>(list));
        batch.doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e)
            {
                if (e==null)//删除成功
                {
                    mDatabaseManager.saveMessage(mList);//先存到数据库
                    loadMsg();
                }else
                {
                    mView.hideProgress();
                    mView.showMsg(e.getMessage());
                }
            }
        });

    }

    private void loadMsg()
    {
        List<UserBean> users = mDatabaseManager.queryAllUsers();//找出所有通讯过的用户
        List<MessageBean> mlist = new ArrayList<>();
        for(UserBean sb : users)
        {
            List<MessageBean> templist = mDatabaseManager.queryMessageByUser(sb.getObjectId(), 1, 0);//找出用户的第一条消息
            if (templist.size() > 0)
            {
                mlist.add(templist.get(0));

            } else//如果不存在，就删除用户
            {
                mDatabaseManager.deleteUser(sb.getObjectId());
            }
        }
        mView.hideProgress();
        mView.showMessage(mlist);
    }

    @Override
    public void deleteMessage(MessageBean bean)
    {
        if (MyApp.isCurrentUser(bean.getSender()))
            mDatabaseManager.deleteUser(bean.getReceiver().getObjectId());
        else
            mDatabaseManager.deleteUser(bean.getSender().getObjectId());
    }
}
