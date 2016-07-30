package com.hadesky.cacw.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * Created by 45517 on 2015/11/22.
 */
public final class Md5Util {
    public static String string2Md5(String txt) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        MessageDigest md5Ins = null;
        try {
            md5Ins = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        byte[] byteArray = txt.getBytes();
        md5Ins.update(byteArray);//更新摘要
        byte[] md = md5Ins.digest();
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (byte byte0 : md) {
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }
}
