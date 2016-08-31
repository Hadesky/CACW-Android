package com.hadesky.cacw.util;

import com.hadesky.cacw.bean.TaskBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 *
 * Created by dzysg on 2016/7/18 0018.
 */
public class TaskComparetor implements Comparator<TaskBean> {




    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    @Override
    public int compare(TaskBean lhs, TaskBean rhs) {

        try {
            Date l = format.parse(lhs.getMstartDate());
            Date r = format.parse(rhs.getEndDate());
            return l.compareTo(r);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
