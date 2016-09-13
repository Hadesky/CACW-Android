package com.hadesky.cacw.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.InvitePersonAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.InvitePersonPresenter;
import com.hadesky.cacw.presenter.InvitePersonPresenterImpl;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.activity.UserInfoActivity;
import com.hadesky.cacw.ui.view.InvitePersonView;

import java.util.List;


public class InvitePersonFragment extends BaseFragment implements InvitePersonView, BaseViewHolder.OnItemClickListener
{


    private EditText mEdtSearch;
    private RecyclerView mRecyclerView;
    private TeamBean mTeamBean;
    private InvitePersonPresenter mPresenter;
    private InvitePersonAdapter mAdapter;
    private List<UserBean> mUser;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_invite_person;
    }

    @Override
    protected void initViews(View view)
    {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);
        mEdtSearch = (EditText) view.findViewById(R.id.et);
    }


    @Override
    protected void setupViews(Bundle bundle)
    {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new InvitePersonAdapter(null, R.layout.item_person_in_invite,this,this);
        mRecyclerView.setAdapter(mAdapter);
        mTeamBean = (TeamBean) getArguments().getSerializable(IntentTag.TAG_TEAM_BEAN);
        mPresenter = new InvitePersonPresenterImpl(this,mTeamBean);

        mEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId== EditorInfo.IME_ACTION_SEARCH)
                {
                    mPresenter.search(v.getText().toString());
                }
                return true;
            }
        });
    }

    private void showInviteDialog(final int position) {

        View view = getLayoutInflater(null).inflate(R.layout.dialog_with_edit_text_30, null);
        final EditText editText = (EditText) view.findViewById(R.id.edit_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.invite_dialog_title))
                .setView(view)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.inviteUser(editText.getText().toString(),position);
                    }
                });
        builder.create().show();
    }

    @Override
    public void showUser(List<UserBean> list,boolean isfinial)
    {
        mUser = list;
        mAdapter.setData(list,isfinial);
    }

    @Override
    public void onInviteSucceed(int pos)
    {
        mAdapter.disableInviteButton(pos);
    }

    @Override
    public void showProgress()
    {

    }

    @Override
    public void hideProgress()
    {

    }

    @Override
    public void showMsg(String s)
    {
        showToast(s);
    }

    @Override
    public void OnItemClick(View view, int position)
    {

        if(position==mAdapter.getNextResultPosition())
        {
            mPresenter.LoadNextPage();
        }
        else if(view.getId()==R.id.layout_invite_item)
        {
            UserBean u = mUser.get(position);
            Intent intent = new Intent(getContext(), UserInfoActivity.class);
            intent.putExtra(IntentTag.TAG_USER_BEAN,(Parcelable)u);
            getContext().startActivity(intent);
        }else if(view.getId()==R.id.bt_invite)
        {
            showInviteDialog(position);
        }
    }
}
