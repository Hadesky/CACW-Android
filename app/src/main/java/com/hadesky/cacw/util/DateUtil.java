package com.hadesky.cacw.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dzysg on 2016/7/16 0016.
 */
public class DateUtil {


    static public SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    }

    static public Date StringToDate(String s) {
        SimpleDateFormat format = getSimpleDateFormat();
        Date date = null;
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
