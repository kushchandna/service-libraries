package com.kush.lib.expressions.utils;

public interface TypedComparator {

    int compare(int val1, int val2);

    int compare(long val1, long val2);

    int compare(float val1, float val2);

    int compare(double val1, double val2);

    int compare(boolean val1, boolean val2);

    int compare(String val1, String val2);
}
