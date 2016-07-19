package com.hadesky.cacw.ui.activity;

import com.hadesky.cacw.R;
import com.hadesky.cacw.ui.widget.SearchView.SearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchTeamActivity extends BaseActivity {

    SearchView mSearchView;
    List<String> mSuggest;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_team;
    }

    @Override
    public void initView() {

        mSearchView = (SearchView) findViewById(R.id.searchview);


    }

    @Override
    public void setupView() {
        mSuggest = new ArrayList<>();
        mSearchView.setListener(new SearchView.SearchListener() {
            @Override
            public void onTextChange(String text) {
                mSuggest.clear();
                if(text.length()>0)
                {
                    mSuggest.add("搜名称: "+text);
                    mSuggest.add("搜id : "+text);
                }
                mSearchView.showSuggest(mSuggest);

            }

            @Override
            public void onSumbit(String text) {
                showToast("进行搜索  "+text);
            }
        });
    }
}
