package com.hadesky.cacw.util;

import android.content.Context;
import android.support.annotation.Nullable;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.MessageBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by 45517 on 2016/7/22.
 */
public class StringUtils {

    /**
     * 判断是否为电话号码
     *
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
    public static String composeInviteOrJoinString(String teamId, String teamName, String message) {
        if (teamId == null || message == null || teamId.length() == 0) {
            //不允许teamId为空
            return null;
        }
        if (message.length() == 0) {
            return teamId + '$' + teamName;
        }
        return teamId + '$' + teamName + '$' + message;
    }

    public static String removeBrackets(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }
        if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')') {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    public static String roundWithBrackets(String s) {
        if (s == null) {
            return null;
        }
        return '(' + s + ')';
    }

    /**
     * 根据拿到的Bean来生成显示给用户看的Message
     *
     * @param bean 要生成message的Bean
     * @return message
     */
    public static String messageBean2Msg(MessageBean bean, Context context) {
        if (bean == null || context == null) {
            return "";
        }
        if (bean.getType().equals(MessageBean.TYPE_USER_TO_USER)) {
            return bean.getMsg();
        }
        if (bean.getType().equals(MessageBean.TYPE_TEAM_TO_USER)) {
            //邀请新成员
            String[] defMsg = context.getResources().getStringArray(R.array.default_invite_message);
            String[] cut = cutInviteOrJoinString(bean.getMsg());

            String headMsg = String.format(defMsg[0], bean.getSender().getNickName(), cut[1]);

            if (cut.length == 2) {
                //只包含TeamId和teamName
                return headMsg + defMsg[2];
            } else {
//                含有msg
                String additionMsg = String.format(defMsg[1], cut[2]);
                return headMsg + additionMsg + defMsg[2];
            }
        }
        if (bean.getType().equals(MessageBean.TYPE_USER_TO_TEAM)) {
            //申请加入
            String[] defMsg = context.getResources().getStringArray(R.array.default_join_message);
            String[] cut = cutInviteOrJoinString(bean.getMsg());

            String headMsg = String.format(defMsg[0], bean.getSender().getNickName(), cut[1]);

            if (cut.length == 2) {
                //只包含TeamId和teamName
                return headMsg + defMsg[2];
            } else {
//                含有msg
                String additionMsg = String.format(defMsg[1], cut[2]);
                return headMsg + additionMsg + defMsg[2];
            }
        }
        return "";
    }

    /**
     * 根据获取到的MessageBean解析得到TeamId
     */
    @Nullable
    public static String getTeamIdByMessageBean(MessageBean bean) {
        if (bean == null || bean.getType().equals(MessageBean.TYPE_USER_TO_USER)) {
            return null;
        }
        String[] cut = cutInviteOrJoinString(bean.getMsg());
        return cut[0];
    }

    /**
     * 分割邀请字段的字符串，注意判断长度，length为1时只包含teamId，为2时0是teamId，1是msg
     *
     * @param inviteString 拿到的消息内容
     * @return length为2时只包含teamId和teamName，为3时0是teamId，1是teamName,2是msg
     */
    public static String[] cutInviteOrJoinString(String inviteString) {
        if (inviteString == null) {
            return null;
        }
        int[] d = new int[2];
        for (int i = 0,j = 0; i < inviteString.length(); i++) {
            if (j <= 1 && inviteString.charAt(i) == '$') {
                d[j] = i;
                j++;
                if (j == 2)
                    break;
            }
        }
        if (d[1] == 0) {
            //没有附带msg,直接返回的就是teamId和teamName
            String[] result = new String[2];
            result[0] = inviteString.substring(0, d[0]);
            result[1] = inviteString.substring(d[0] + 1, inviteString.length());
            return result;
        }
        String[] result = new String[3];
        result[0] = inviteString.substring(0, d[0]);
        result[1] = inviteString.substring(d[0] + 1, d[1]);
        result[2] = inviteString.substring(d[1] + 1, inviteString.length());
        return result;
    }
}
