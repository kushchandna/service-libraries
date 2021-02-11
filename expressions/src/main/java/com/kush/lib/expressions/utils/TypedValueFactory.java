package com.kush.lib.expressions.utils;

import static com.kush.lib.expressions.Type.BOOLEAN;
import static com.kush.lib.expressions.Type.DOUBLE;
import static com.kush.lib.expressions.Type.FLOAT;
import static com.kush.lib.expressions.Type.INTEGER;
import static com.kush.lib.expressions.Type.LONG;
import static com.kush.lib.expressions.Type.STRING;

import com.kush.lib.expressions.Type;
import com.kush.lib.expressions.TypedValue;

public class TypedValueFactory {

    public static TypedValue intValue(int value) {
        return new BaseTypedValue(INTEGER) {

            @Override
            public int getInt() {
                return value;
            }

            @Override
            public Object getObject() {
                return Integer.valueOf(value);
            }

            @Override
            protected boolean valueEquals(BaseTypedValue other) {
                return value == other.getInt();
            }

            @Override
            protected int valueHashCode() {
                return Integer.hashCode(value);
            }
        };
    }

    public static TypedValue longValue(long value) {
        return new BaseTypedValue(LONG) {

            @Override
            public long getLong() {
                return value;
            }

            @Override
            public Object getObject() {
                return Long.valueOf(value);
            }

            @Override
            protected boolean valueEquals(BaseTypedValue other) {
                return value == other.getLong();
            }

            @Override
            protected int valueHashCode() {
                return Long.hashCode(value);
            }
        };
    }

    public static TypedValue floatValue(float value) {
        return new BaseTypedValue(FLOAT) {

            @Override
            public float getFloat() {
                return value;
            }

            @Override
            public Object getObject() {
                return Float.valueOf(value);
            }

            @Override
            protected boolean valueEquals(BaseTypedValue other) {
                return value == other.getFloat();
            }

            @Override
            protected int valueHashCode() {
                return Float.hashCode(value);
            }
        };
    }

    public static TypedValue doubleValue(double value) {
        return new BaseTypedValue(DOUBLE) {

            @Override
            public double getDouble() {
                return value;
            }

            @Override
            public Object getObject() {
                return Double.valueOf(value);
            }

            @Override
            protected boolean valueEquals(BaseTypedValue other) {
                return value == other.getDouble();
            }

            @Override
            protected int valueHashCode() {
                return Double.hashCode(value);
            }
        };
    }

    public static TypedValue booleanValue(boolean value) {
        return new BaseTypedValue(BOOLEAN) {

            @Override
            public boolean getBoolean() {
                return value;
            }

            @Override
            public Object getObject() {
                return Boolean.valueOf(value);
            }

            @Override
            protected boolean valueEquals(BaseTypedValue other) {
                return value == other.getBoolean();
            }

            @Override
            protected int valueHashCode() {
                return Boolean.hashCode(value);
            }
        };
    }

    public static TypedValue stringValue(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return new StringValue(value);
    }

    public static TypedValue nullValue(Type type) {
        if (type == STRING) {
            return new StringValue(null) {

                @Override
                public boolean isNull() {
                    return true;
                }
            };
        } else {
            return new BaseTypedValue(type) {

                @Override
                public boolean isNull() {
                    return true;
                }

                @Override
                public Object getObject() {
                    return null;
                }

                @Override
                protected boolean valueEquals(BaseTypedValue other) {
                    return true;
                }

                @Override
                protected int valueHashCode() {
                    return SpecialValues.NULL_INT;
                }
            };
        }
    }

    public static TypedValue nullableValue(Object value, Type type) {
        if (value == null) {
            return nullValue(type);
        }
        switch (type) {
        case BOOLEAN:
            return booleanValue((Boolean) value);
        case DOUBLE:
            return doubleValue((Double) value);
        case FLOAT:
            return floatValue((Float) value);
        case INTEGER:
            return intValue((Integer) value);
        case LONG:
            return longValue((Long) value);
        case STRING:
            return stringValue((String) value);
        default:
            throw new UnsupportedOperationException();
        }
    }

    private static class StringValue extends BaseTypedValue {

        private final String value;

        public StringValue(String value) {
            super(STRING);
            this.value = value;
        }

        @Override
        public String getString() {
            return value;
        }

        @Override
        public Object getObject() {
            return value;
        }

        @Override
        protected boolean valueEquals(BaseTypedValue other) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected int valueHashCode() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + (value == null ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            StringValue other = (StringValue) obj;
            if (value == null) {
                if (other.value != null) {
                    return false;
                }
            } else if (!value.equals(other.value)) {
                return false;
            }
            return true;
        }
    }

    private static abstract class BaseTypedValue implements TypedValue {

        private final Type type;

        public BaseTypedValue(Type type) {
            this.type = type;
        }

        @Override
        public boolean isNull() {
            return false;
        }

        @Override
        public int getInt() {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getDouble() {
            throw new UnsupportedOperationException();
        }

        @Override
        public float getFloat() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getLong() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean getBoolean() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getString() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getObject() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Type getType() {
            return type;
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
            switch (o1.getType()) {
            case BOOLEAN:
                return Boolean.compare(o1.getBoolean(), o2.getBoolean());
            case DOUBLE:
                return Double.compare(o1.getDouble(), o2.getDouble());
            case FLOAT:
                return Float.compare(o1.getFloat(), o2.getFloat());
            case INTEGER:
                return Integer.compare(o1.getInt(), o2.getInt());
            case LONG:
                return Long.compare(o1.getLong(), o2.getLong());
            case STRING:
                return o1.getString().compareTo(o2.getString());
            default:
                throw new IllegalStateException();
            }
        }

        @Override
        public String toString() {
            return String.format("%s (%s)", String.valueOf(getObject()), type);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (type == null ? 0 : type.hashCode());
            result = prime * result + valueHashCode();
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
            if (type != other.type) {
                return false;
            }
            if (isNull() != other.isNull()) {
                return false;
            }
            return valueEquals(other);
        }

        protected abstract boolean valueEquals(BaseTypedValue other);

        protected abstract int valueHashCode();
    }

    private TypedValueFactory() {
        throw new UnsupportedOperationException();
    }
}
