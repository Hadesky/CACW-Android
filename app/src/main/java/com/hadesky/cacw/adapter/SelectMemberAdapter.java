package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.CheckBox;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.base.BaseAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.UserBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  选择任务成员
 * Created by dzysg on 2015/10/16 0016.
 */
public class SelectMemberAdapter extends BaseAdapter<UserBean>
{


    private Map<Integer, Boolean> mSelectMap = new HashMap<>();



    public SelectMemberAdapter(List<UserBean> datas, @LayoutRes int id) {
        super(datas,id);
        for (int i = 0; i < mDatas.size(); i++) {
            mSelectMap.put(i, false);
        }
    }

    @Override
    public BaseViewHolder<UserBean> createHolder(View v, Context context) {


        BaseViewHolder<UserBean> holder = new BaseViewHolder<UserBean>(v) {
            @Override
            public void setData(UserBean bean) {
                setCheckBox(R.id.cb,mSelectMap.get(getLayoutPosition()));
            }
        };
        holder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
                boolean b = !cb.isChecked();
                cb.setChecked(b);
                mSelectMap.put(pos, b);
            }
        });
        return holder;

    }

    public void selectAll() {
        for (Map.Entry<Integer, Boolean> item : mSelectMap.entrySet()) {
            item.setValue(true);
        }
        notifyDataSetChanged();
    }

    public void reverse() {
        for (Map.Entry<Integer, Boolean> item : mSelectMap.entrySet()) {
            item.setValue(!item.getValue());
        }
        notifyDataSetChanged();
    }


}
