package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TeamBean;

import java.util.List;

/**
 * 我的团队adapter
 * Created by 45517 on 2016/3/21.
 */
public class MyTeamAdapter extends BaseAdapter<TeamBean>
{

    public MyTeamAdapter(List<TeamBean> list, @LayoutRes int layoutid) {
        super(list, layoutid);
    }

    @Override
    public BaseViewHolder<TeamBean> createHolder(View v, Context context) {

        BaseViewHolder<TeamBean> holder = new BaseViewHolder<TeamBean>(v) {
            @Override
            public void setData(TeamBean teamBean) {

            }
        };
        return holder;
    }



}
