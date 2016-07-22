package com.hadesky.cacw.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 45517 on 2016/7/22.
 */
public class StringUtils {
    /**
     * 判断是否为电话号码
     * @param s 判断的字符串
     * @return 是否电话号码
     */
    public static boolean isPhoneNumber(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("0?(13|14|15|18)[0-9]{9}");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public static boolean isEmail(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public static boolean isAllDigest(String key) {
        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) < '0' || key.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }
}
