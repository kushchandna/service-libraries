package com.kush.lib.expressions.types.factory;

import static com.kush.lib.expressions.types.factory.TypedValueFactory.booleanValue;
import static com.kush.lib.expressions.types.factory.TypedValueFactory.byteValue;
import static com.kush.lib.expressions.types.factory.TypedValueFactory.charValue;
import static com.kush.lib.expressions.types.factory.TypedValueFactory.doubleValue;
import static com.kush.lib.expressions.types.factory.TypedValueFactory.floatValue;
import static com.kush.lib.expressions.types.factory.TypedValueFactory.intValue;
import static com.kush.lib.expressions.types.factory.TypedValueFactory.longValue;
import static com.kush.lib.expressions.types.factory.TypedValueFactory.stringValue;

import com.kush.lib.expressions.types.ImpactedByAutoBoxing;
import com.kush.lib.expressions.types.TypedValue;
import com.kush.lib.expressions.types.processors.TypeProcessor;

@ImpactedByAutoBoxing
class NonNullTypedValueGenerator extends TypeProcessor<TypedValue> {

    private final Object value;

    public NonNullTypedValueGenerator(Object value) {
        this.value = value;
    }

    @Override
    protected TypedValue handleBoolean() {
        return booleanValue((Boolean) value);
    }

    @Override
    protected TypedValue handleByte() {
        return byteValue((Byte) value);
    }

    @Override
    protected TypedValue handleChar() {
        return charValue((Character) value);
    }

    @Override
    protected TypedValue handleInt() {
        return intValue((Integer) value);
    }

    @Override
    protected TypedValue handleLong() {
        return longValue((Long) value);
    }

    @Override
    protected TypedValue handleFloat() {
        return floatValue((Float) value);
    }

    @Override
    protected TypedValue handleDouble() {
        return doubleValue((Double) value);
    }

    @Override
    protected TypedValue handleString() {
        return stringValue((String) value);
    }

    @Override
    protected TypedValue handleObject() {
        throw new UnsupportedOperationException();
    }
}
