package com.hadesky.cacw.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.MessageListAdapter;
import com.hadesky.cacw.adapter.base.BaseAdapter;
import com.hadesky.cacw.bean.MessageBean;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void setupView()
    {
        mAdapter = new MessageListAdapter(new ArrayList<MessageBean>(), R.layout.list_item_message);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mListPresenter.onDestroy();
    }
}
