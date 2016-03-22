package com.hadesky.cacw.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TeamBean;

import java.util.List;

/** 我的团队adapter
 * Created by 45517 on 2016/3/21.
 */
public class MyTeamAdapter extends BaseRvAdapter<MyTeamAdapter.VHolder>
{

    private List<TeamBean> mDatas;

    public MyTeamAdapter(Context mContext)
    {
        super(mContext);
    }

    public void setDatas(List<TeamBean> datas)
    {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.list_item_team,parent,false);


        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(VHolder holder, int position)
    {
        holder.mTeamName.setText(mDatas.get(position).getTeamName());
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    public static class VHolder extends BaseViewHolder
    {

        TextView mTeamName;
        TextView mMembersCount;


        public VHolder(View itemView)
        {
            super(itemView);
            mTeamName = (TextView) itemView.findViewById(R.id.team_name);
            mMembersCount = (TextView) itemView.findViewById(R.id.members_count);

        }

        @Override
        public void initView(View itemView)
        {

        }
    }
}
