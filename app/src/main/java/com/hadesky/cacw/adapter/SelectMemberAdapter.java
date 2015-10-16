package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TaskMemberBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dzysg on 2015/10/16 0016.
 */
public class SelectMemberAdapter extends RecyclerView.Adapter<SelectMemberAdapter.SelectMemberVH>
{

    private List<TaskMemberBean> mDatas;
    private Context              mContext;
    private Map<Integer, Boolean> mSelectMap = new HashMap<>();

    public SelectMemberAdapter(Context context, List<TaskMemberBean> datas)
    {
        mDatas = datas;
        mContext = context;
        for (int i = 0; i < mDatas.size(); i++) {
            mSelectMap.put(i, false);
        }
    }

    @Override
    public SelectMemberVH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_select_members, parent, false);
        SelectMemberVH holder = new SelectMemberVH(v, new SelectMemberVH.OnItemClickListener()
        {
            @Override
            public void onClick(View view, int pos)
            {
                CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
                boolean b = !cb.isChecked();
                cb.setChecked(b);
                mSelectMap.put(pos,b);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SelectMemberVH holder, int position)
    {
        holder.cb.setChecked(mSelectMap.get(position));
    }

    public void selectAll()
    {
        for(Map.Entry<Integer,Boolean> item:mSelectMap.entrySet())
        {
            item.setValue(true);
        }
        notifyDataSetChanged();
    }
    public void reverse()
    {
        for(Map.Entry<Integer,Boolean> item:mSelectMap.entrySet())
        {
            item.setValue(!item.getValue());
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    public static class SelectMemberVH extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        private OnItemClickListener mListener;
        public  CheckBox            cb;

        public SelectMemberVH(View itemView, OnItemClickListener listener)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            mListener = listener;
            cb = (CheckBox) itemView.findViewById(R.id.cb);
        }

        @Override
        public void onClick(View v)
        {
            if (mListener != null) mListener.onClick(v, getAdapterPosition());
        }

        public interface OnItemClickListener
        {
            public void onClick(View view, int pos);

        }

    }
}
