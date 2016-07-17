package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.ui.activity.TeamInfoActivity;
import com.hadesky.cacw.util.ImageUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;

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
    public BaseViewHolder<TeamMember> createHolder(final View v, Context context) {

        BaseViewHolder<TeamMember> holder = new BaseViewHolder<TeamMember>(v) {
            @Override
            public void setData(TeamMember teamBean) {
                setTextView(R.id.tv_team_name,teamBean.getTeam().getTeamName());
                setTextView(R.id.tv_team_summary, teamBean.getTeam().getSummary());

                BmobQuery<TeamMember> query = new BmobQuery<>();
                query.addWhereEqualTo("mTeam", new BmobPointer(teamBean.getTeam()));
                query.findObjects(new FindListener<TeamMember>() {
                    @Override
                    public void done(List<TeamMember> list, BmobException e) {
                        if (e==null) {
                            View view = findView(R.id.layout_member_count);
                            view.setVisibility(View.VISIBLE);
                            setTextView(R.id.tv_member_count, "" + list.size());
                        }
                    }
                });
                ImageView view = findView(R.id.sdv_team_icon);
                if (teamBean.getTeam().getTeamAvatar()!=null)
                    view.setImageURI(Uri.parse(teamBean.getTeam().getTeamAvatar().getUrl()));
                Button newProjectBt = findView(R.id.bt_new_project);
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
