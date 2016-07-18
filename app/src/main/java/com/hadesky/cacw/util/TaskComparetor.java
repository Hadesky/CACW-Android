package com.hadesky.cacw.util;

import com.hadesky.cacw.bean.TaskMember;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 *
 * Created by dzysg on 2016/7/18 0018.
 */
public class TaskComparetor implements Comparator<TaskMember> {




    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    @Override
    public int compare(TaskMember lhs, TaskMember rhs) {

        try {
            Date l = format.parse(lhs.getTask().getStartDate().getDate());
            Date r = format.parse(rhs.getTask().getStartDate().getDate());
            return l.compareTo(r);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
