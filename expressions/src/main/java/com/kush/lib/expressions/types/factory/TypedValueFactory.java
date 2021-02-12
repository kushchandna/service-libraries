package com.kush.lib.expressions.types.factory;

import com.kush.lib.expressions.types.ImpactedByAutoBoxing;
import com.kush.lib.expressions.types.Type;
import com.kush.lib.expressions.types.TypedValue;

public class TypedValueFactory {

    public static TypedValue booleanValue(boolean value) {
        return new BooleanValue(value);
    }

    public static TypedValue byteValue(byte value) {
        return new ByteValue(value);
    }

    public static TypedValue charValue(char value) {
        return new CharValue(value);
    }

    public static TypedValue intValue(int value) {
        return new IntValue(value);
    }

    public static TypedValue longValue(long value) {
        return new LongValue(value);
    }

    public static TypedValue floatValue(float value) {
        return new FloatValue(value);
    }

    public static TypedValue doubleValue(double value) {
        return new DoubleValue(value);
    }

    public static TypedValue stringValue(String value) {
        return new StringValue(value);
    }

    public static TypedValue nullValue(Type type) {
        return new NullValue(type);
    }

    @ImpactedByAutoBoxing
    public static TypedValue nullableValue(Object value, Type type) {
        if (value == null) {
            return nullValue(type);
        }
        NonNullTypedValueGenerator generator = new NonNullTypedValueGenerator(value);
        return generator.process(type);
    }
}
