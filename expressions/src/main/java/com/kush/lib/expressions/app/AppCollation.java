package com.kush.lib.expressions.app;

public class AppCollation {

    private static boolean isNullHigh = false;

    public static boolean isNullHigh() {
        return isNullHigh;
    }

    public static void setNullHigh() {
        isNullHigh = true;
    }

    public static void setNullLow() {
        isNullHigh = false;
    }
}
