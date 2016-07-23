package com.hadesky.cacw.ui.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.base.BaseAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.presenter.MessageListPresenter;
import com.hadesky.cacw.presenter.MessageListPresenterImpl;
import com.hadesky.cacw.ui.view.MessageListView;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends BaseActivity implements MessageListView
{


    private RecyclerView mRecyclerView;
    private BaseAdapter<MessageBean> mAdapter;
    private AnimProgressDialog mProgressDialog;
    private MessageListPresenter mListPresenter;

    @Override
    public int getLayoutId()
    {
        return R.layout.activity_message_list;
    }

    @Override
    public void initView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_message);
    }

    @Override
    public void setupView()
    {

        mAdapter = new BaseAdapter<MessageBean>(new ArrayList<MessageBean>(), R.layout.list_item_message)
        {
            @Override
            public BaseViewHolder<MessageBean> createHolder(View v, Context context)
            {
                BaseViewHolder<MessageBean> holder = new BaseViewHolder<MessageBean>(v)
                {
                    @Override
                    public void setData(MessageBean messageBean)
                    {
                        UserBean u = messageBean.getReceiver();
                        if (MyApp.isCurrentUser(u))
                        {
                            setTextView(R.id.tv_nick_name, messageBean.getSender().getNickName());
                            u = messageBean.getSender();
                        } else
                        {
                            setTextView(R.id.tv_nick_name, u.getNickName());
                        }
                        setTextView(R.id.tv_msg, messageBean.getMsg());
                        SimpleDraweeView iv = findView(R.id.iv_avatar);
                        iv.setImageURI(u.getAvatarUrl());
                    }
                };

                return holder;
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);


        mListPresenter = new MessageListPresenterImpl(this);
        mListPresenter.LoadMessage();
    }

    @Override
    public void showMessage(List<MessageBean> list)
    {
        mAdapter.setDatas(list);
    }

    @Override
    public void showProgress()
    {
        if (mProgressDialog == null)
        {
            mProgressDialog = new AnimProgressDialog(this, "获取中...");
        }
        mProgressDialog.show();
    }

    @Override
    public void hideProgress()
    {
        mProgressDialog.dismiss();
    }

    @Override
    public void showMsg(String s)
    {
        showToast(s);
    }
}
