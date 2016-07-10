package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TeamMember;

import java.util.List;

/**
 * 我的团队adapter
 * Created by 45517 on 2016/3/21.
 */
public class MyTeamAdapter extends BaseAdapter<TeamMember>
{

    public MyTeamAdapter(List<TeamMember> list, @LayoutRes int layoutid) {
        super(list, layoutid);
    }

    @Override
    public BaseViewHolder<TeamMember> createHolder(View v, Context context) {

        BaseViewHolder<TeamMember> holder = new BaseViewHolder<TeamMember>(v) {
            @Override
            public void setData(TeamMember teamBean) {
                setTextView(R.id.mTeamName,teamBean.getTeam().getTeamName());
            }
        };
        return holder;
    }
}
