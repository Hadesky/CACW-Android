package com.hadesky.cacw.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.MyTaskListAdapter;
import com.hadesky.cacw.bean.TaskBean;

import java.util.ArrayList;
import java.util.List;

/**我的任务Fragment
 * Created by Bright Van on 2015/9/7/007.
 */
public class MyTaskFragment extends BaseFragment {

    private ListView mListView;
    private MyTaskListAdapter mAdapter;
    private List<TaskBean> mDatas;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_task;
    }

    @Override
    protected void initViews(View view) {

        mListView = (ListView) mContentView.findViewById(R.id.lv_task_my_task_fragment);

    }

    @Override
    protected void setupViews(Bundle bundle) {

        mDatas = new ArrayList<TaskBean>();
        for (int i = 0; i < 10; i++) {
            mDatas.add(new TaskBean());
        }
        mAdapter = new MyTaskListAdapter(getContext(),mDatas);
        mListView.setAdapter(mAdapter);

    }
}
