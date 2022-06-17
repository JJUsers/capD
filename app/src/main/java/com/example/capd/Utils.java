package com.example.capd;

import android.text.TextUtils;

public class Utils {

    /* 숫자 체크 */
    public static boolean isNumeric(String str) {
        boolean chk = false;

        try{
            Double.parseDouble(str) ;
            chk = true ;
        } catch (Exception ignored) {}

        return chk;
    }

    /* 시간 표현 값 얻기(시,분) */
    public static String getDisplayTime(long millis) {
        StringBuilder str = new StringBuilder();

        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;

        if(hour > 0) {
            str.append(hour).append("시간");
        }
        if(minute > 0) {
            str.append(minute).append("분");
        }
        if(second > 0) {
            str.append(second).append("초");
        }

        if (TextUtils.isEmpty(str.toString())) {
            str.append("0초");
        }

        return str.toString();
    }

}
