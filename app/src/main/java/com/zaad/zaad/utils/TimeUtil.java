package com.zaad.zaad.utils;

public class TimeUtil {

    public static String getHours(int hours, String ampm) {
        if (hours < 12) {
            return hours + ampm;
        }
        return hours - 12 + ampm;
    }
}
