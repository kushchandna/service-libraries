package com.kush.lib.expressions.types.factory;

import com.kush.lib.expressions.types.TypedValue;

abstract class BaseTypedValue implements TypedValue {

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean getBoolean() {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getByte() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getChar() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getInt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLong() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getFloat() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getDouble() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(TypedValue o) {
        TypedValue o1 = this;
        TypedValue o2 = o;
        if (o1.getType() != o2.getType()) {
            throw new IllegalArgumentException();
        }
        if (o1.isNull() || o2.isNull()) {
            throw new IllegalStateException();
        }
        NonNullTypedComparisionProcessor processor = new NonNullTypedComparisionProcessor(o1, o2);
        return processor.process(getType());
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", String.valueOf(getObject()), getType());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getType() == null ? 0 : getType().hashCode());
        result = prime * result + (isNull() ? 0 : nonNullValueHashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BaseTypedValue other = (BaseTypedValue) obj;
        if (getType() != other.getType()) {
            return false;
        }
        if (isNull() != other.isNull()) {
            return false;
        }
        if (isNull() && other.isNull()) {
            return true;
        }
        return nonNullValueEquals(other);
    }

    protected abstract boolean nonNullValueEquals(BaseTypedValue other);

    protected abstract int nonNullValueHashCode();
}
