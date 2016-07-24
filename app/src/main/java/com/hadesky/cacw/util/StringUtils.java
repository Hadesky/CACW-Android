package com.hadesky.cacw.util;

import android.support.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
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

    /**
     * 组成邀请消息字符串
     */
    @Nullable
    public static String composeInviteString(String teamId, String message) {
        if (teamId == null || message == null || teamId.length() == 0) {
            //不允许teamId为空
            return null;
        }
        if (message.length() == 0) {
            return teamId;
        }
        return teamId + '$' + message;
    }


    /**
     * 分割邀请字段的字符串，注意判断长度，length为1时只包含teamId，为2时0是teamId，1是msg
     * @param inviteString 拿到的消息内容
     * @return length为1时只包含teamId，为2时0是teamId，1是msg
     */
    public static String[] cutInviteString(String inviteString) {
        if (inviteString == null) {
            return null;
        }
        int divisionPosition = 0;
        for (int i = 0; i < inviteString.length(); i++) {
            if (inviteString.charAt(i) == '$') {
                divisionPosition = i;
                break;
            }
        }
        if (divisionPosition == 0) {
            //没有附带msg,直接返回的就是teamId
            return new String[]{inviteString};
        }
        String[] result = new String[2];
        result[0] = inviteString.substring(0, divisionPosition);
        result[1] = inviteString.substring(divisionPosition + 1, inviteString.length());
        return result;
    }


    public void getMsgContent(String msg)
    {

    }
}
