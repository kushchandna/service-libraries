package com.kush.lib.expressions.types;

public class SpecialValues {

    public static final int NULL_INT = Integer.MIN_VALUE;
    public static final long NULL_LONG = Long.MIN_VALUE;
    public static final float NULL_FLOAT = Float.NEGATIVE_INFINITY;
    public static final double NULL_DOUBLE = Double.NEGATIVE_INFINITY;

    private SpecialValues() {
        throw new UnsupportedOperationException();
    }
}
