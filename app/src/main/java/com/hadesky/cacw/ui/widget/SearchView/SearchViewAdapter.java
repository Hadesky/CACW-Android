package com.hadesky.cacw.ui.widget.SearchView;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.BaseAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;

import java.util.List;

/**
 *
 * Created by dzysg on 2016/7/19 0019.
 */
public class SearchViewAdapter extends BaseAdapter<String> {

    private SearchView mSearchView;
    public SearchViewAdapter(List<String> list, @LayoutRes int layoutid,SearchView searchView ) {
        super(list, layoutid);
        mSearchView = searchView;
    }

    @Override
    public BaseViewHolder<String> createHolder(View v, Context context) {
        BaseViewHolder<String> holder = new BaseViewHolder<String>(v) {
            @Override
            public void setData(String s) {
                setTextView(R.id.text,s);
            }
        };
        holder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                mSearchView.performQuery(mDatas.get(position));
            }
        });
        return holder;
    }
}
