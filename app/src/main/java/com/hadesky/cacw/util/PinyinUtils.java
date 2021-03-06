package com.hadesky.cacw.util;

import com.microstudent.app.bouncyfastscroller.utils.CharacterUtils;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * Created by MicroStudent on 2016/4/30.
 */
public class PinyinUtils {
    public static char getPinyinLetter(String s, int index) {
        if (!s.isEmpty() && index >= 0 && index < s.length()) {
            String[] result = PinyinHelper.toHanyuPinyinStringArray(s.charAt(index));
            if (result != null) {
                return Character.toUpperCase(result[0].charAt(0));
            } else if (Character.isLetter(s.charAt(index))){
                return Character.toUpperCase(s.charAt(index));
            }
        }
        return ' ';
    }

    public static char getFirstPinyinLetter(String s) {
        if (s != null && !s.isEmpty()) {
            return getPinyinLetter(s, 0);
        }
        return ' ';
    }
}
