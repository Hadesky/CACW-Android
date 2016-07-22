package com.hadesky.cacw.ui.widget.SearchView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadesky.cacw.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dzysg on 2016/7/19 0019.
 */
public class SearchView extends FrameLayout implements View.OnClickListener {


    private EditText mEditText;
    private RecyclerView mRcvSuggest;
    private ImageView mIvBack;
    private ImageView mIvClear;
    private ImageView mIvSearch;
    private View mBg;
    private SearchListener mListener;
    private SearchViewAdapter mAdapter;


    private boolean isInSearchState = false;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.search_view, this, true);
        mEditText = (EditText) findViewById(R.id.edt_input);
        mIvBack = (ImageView) findViewById(R.id.iv_arrow_back);
        mIvClear = (ImageView) findViewById(R.id.iv_clear);
        mRcvSuggest = (RecyclerView) findViewById(R.id.recyclerView_suggest);
        mBg = findViewById(R.id.bg);
        mIvSearch = (ImageView) findViewById(R.id.iv_search);

        mIvBack.setVisibility(View.GONE);
        mIvClear.setVisibility(View.GONE);
        mRcvSuggest.setVisibility(View.GONE);
        mIvBack.setOnClickListener(this);
        mIvClear.setOnClickListener(this);
        mBg.setOnClickListener(this);
        mAdapter = new SearchViewAdapter(new ArrayList<String>(), R.layout.list_item_search, this);
        mRcvSuggest.setLayoutManager(new LinearLayoutManager(context));
        mRcvSuggest.setAdapter(mAdapter);

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    performFocus();
                else
                    performClose();
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                performQuery();
                return false;
            }
        });

        mEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (isInSearchState) {
                        Log.e("tag", "back");
                        performClose();
                        return true;
                    }
                }
                return false;
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextChange();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditText.setHint("团队名称/团队ID");
    }

    private void onTextChange() {

        if (mEditText.getText().toString().length() > 0) {
            if (mIvClear.getVisibility() == View.GONE)
                mIvClear.setVisibility(View.VISIBLE);
        }
        if (mListener != null)
            mListener.onTextChange(mEditText.getText().toString());

    }

    private void performFocus() {
        if (mIvBack.getVisibility() == View.GONE)
            mIvBack.setVisibility(View.VISIBLE);
        if (mIvSearch.getVisibility() == View.VISIBLE)
            mIvSearch.setVisibility(View.GONE);

        if (mEditText.getText().length()>0)
            mIvClear.setVisibility(View.VISIBLE);


        mBg.setVisibility(View.VISIBLE);
        isInSearchState = true;

    }

    private void performClose() {
        if (mIvBack.getVisibility() == View.VISIBLE)
            mIvBack.setVisibility(View.GONE);
        if (mIvClear.getVisibility() == View.VISIBLE)
            mIvClear.setVisibility(View.GONE);

        if (mIvSearch.getVisibility() == View.GONE)
            mIvSearch.setVisibility(View.VISIBLE);

        if (mRcvSuggest.getVisibility() == View.VISIBLE)
            mRcvSuggest.setVisibility(View.GONE);

        if (mBg.getVisibility() == View.VISIBLE)
            mBg.setVisibility(View.GONE);


        mEditText.clearFocus();
        hideKeyBoard();
        isInSearchState = false;
    }


    protected void performQuery(String text) {
        performClose();
        if (mListener != null)
            mListener.onSubmit(text);
    }


    private void performQuery() {
        performQuery(mEditText.getText().toString());
    }


    private void hideKeyBoard() {
        if (!isInEditMode()) {
            InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mIvClear) {
            mEditText.setText("");
            mIvClear.setVisibility(View.GONE);
        } else if (v == mIvBack) {
            performClose();
        } else if (v == mBg) {
            performClose();
        }
    }


    public void showSuggest(List<String> list) {
        if (mRcvSuggest.getVisibility() == View.GONE) {
            mRcvSuggest.setVisibility(View.VISIBLE);
        }

        mAdapter.setDatas(list);
    }

    public void setHint(String s) {
        mEditText.setHint(s);
    }

    public SearchListener getListener() {
        return mListener;
    }

    public void setListener(SearchListener listener) {
        mListener = listener;
    }

    public interface SearchListener {
        void onTextChange(String text);

        void onSubmit(String text);
    }
}
