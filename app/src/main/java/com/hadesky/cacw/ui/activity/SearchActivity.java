package com.hadesky.cacw.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.hadesky.cacw.R;
import com.hadesky.cacw.ui.fragment.SearchPersonFragment;

public class SearchActivity extends BaseActivity {

    private EditText mSearchEditText;
    private FragmentManager mFragmentManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        mSearchEditText = (EditText) findViewById(R.id.et);
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    public void setupView() {
        View view = findViewById(R.id.iv_arrow_back);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SearchPersonFragment fragment = (SearchPersonFragment) getPersonFragment();
                if (fragment == null) {
                    fragment = SearchPersonFragment.newInstance(s.toString());
                    mFragmentManager.beginTransaction()
                            .add(R.id.container_person, fragment)
                            .commit();
                } else {
                    fragment.updateSearchKey(s.toString());
                }
            }
        });
    }

    private Fragment getPersonFragment() {
        return mFragmentManager.findFragmentById(R.id.container_person);
    }
}
