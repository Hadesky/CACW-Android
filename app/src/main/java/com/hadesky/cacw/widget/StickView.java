package com.hadesky.cacw.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hadesky.cacw.R;

/**
 * Created by 45517 on 2015/10/17.
 */
public class StickView extends LinearLayout {
    private TextView titleView;
    private TextView countView;
    private ImageView redPoint;
    private String title;

    private int taskCount;

    public StickView(Context context) {
        this(context, null);
    }

    public StickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_stick, this, true);
        titleView = (TextView) findViewById(R.id.tv_title);
        countView = (TextView) findViewById(R.id.tv_count);

        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.StickView);
        final int N = typedArray.getIndexCount();
        System.out.println(N);
        for (int i = 0; i < N; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.StickView_stick_title:
                    title = typedArray.getString(attr);
                    break;
            }
        }
        typedArray.recycle();
        titleView.setText(title);
    }

    public void setStickTitle(String title) {
        this.title = title;
        titleView.setText(title);
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
        countView.setText(taskCount + "个");
    }
}
