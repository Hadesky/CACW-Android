package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.ui.activity.TeamInfoActivity;

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

                ImageView view = findView(R.id.sdv_team_icon);
                if (teamBean.getTeam().getTeamAvatar()!=null)
                    view.setImageURI(Uri.parse(teamBean.getTeam().getTeamAvatar().getUrl()));
            }
        };
        holder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                TeamBean teamBean = mDatas.get(position).getTeam();
                Intent intent=  new Intent(mContext, TeamInfoActivity.class);
                intent.putExtra(TeamInfoActivity.IntentTag, teamBean);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }
}
