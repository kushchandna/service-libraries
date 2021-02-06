package com.kush.lib.expressions.utils;

import static com.kush.lib.expressions.ExpressionType.BOOLEAN;
import static com.kush.lib.expressions.ExpressionType.DOUBLE;
import static com.kush.lib.expressions.ExpressionType.FLOAT;
import static com.kush.lib.expressions.ExpressionType.INTEGER;
import static com.kush.lib.expressions.ExpressionType.LONG;
import static com.kush.lib.expressions.ExpressionType.STRING;

import com.kush.lib.expressions.ExpressionResult;
import com.kush.lib.expressions.ExpressionType;

public class ExpressionResultFactory {

    public static ExpressionResult intResult(int value) {
        return new BaseExpressionResult(INTEGER) {

            @Override
            public int getInt() {
                return value;
            }

            @Override
            public Object getObject() {
                return Integer.valueOf(value);
            }

            @Override
            protected boolean valueEquals(BaseExpressionResult other) {
                return value == other.getInt();
            }

            @Override
            protected int valueHashCode() {
                return Integer.hashCode(value);
            }
        };
    }

    public static ExpressionResult longResult(long value) {
        return new BaseExpressionResult(LONG) {

            @Override
            public long getLong() {
                return value;
            }

            @Override
            public Object getObject() {
                return Long.valueOf(value);
            }

            @Override
            protected boolean valueEquals(BaseExpressionResult other) {
                return value == other.getLong();
            }

            @Override
            protected int valueHashCode() {
                return Long.hashCode(value);
            }
        };
    }

    public static ExpressionResult floatResult(float value) {
        return new BaseExpressionResult(FLOAT) {

            @Override
            public float getFloat() {
                return value;
            }

            @Override
            public Object getObject() {
                return Float.valueOf(value);
            }

            @Override
            protected boolean valueEquals(BaseExpressionResult other) {
                return value == other.getFloat();
            }

            @Override
            protected int valueHashCode() {
                return Float.hashCode(value);
            }
        };
    }

    public static ExpressionResult doubleResult(double value) {
        return new BaseExpressionResult(DOUBLE) {

            @Override
            public double getDouble() {
                return value;
            }

            @Override
            public Object getObject() {
                return Double.valueOf(value);
            }

            @Override
            protected boolean valueEquals(BaseExpressionResult other) {
                return value == other.getDouble();
            }

            @Override
            protected int valueHashCode() {
                return Double.hashCode(value);
            }
        };
    }

    public static ExpressionResult booleanResult(boolean value) {
        return new BaseExpressionResult(BOOLEAN) {

            @Override
            public boolean getBoolean() {
                return value;
            }

            @Override
            public Object getObject() {
                return Boolean.valueOf(value);
            }

            @Override
            protected boolean valueEquals(BaseExpressionResult other) {
                return value == other.getBoolean();
            }

            @Override
            protected int valueHashCode() {
                return Boolean.hashCode(value);
            }
        };
    }

    public static ExpressionResult stringResult(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return new StringExpressionResult(value);
    }

    public static ExpressionResult nullResult(ExpressionType type) {
        if (type == STRING) {
            return new StringExpressionResult(null) {

                @Override
                public boolean isNull() {
                    return true;
                }
            };
        } else {
            return new BaseExpressionResult(type) {

                @Override
                public boolean isNull() {
                    return true;
                }

                @Override
                public Object getObject() {
                    return null;
                }

                @Override
                protected boolean valueEquals(BaseExpressionResult other) {
                    return true;
                }

                @Override
                protected int valueHashCode() {
                    return SpecialValues.NULL_INT;
                }
            };
        }
    }

    public static ExpressionResult nullableResult(Object value, ExpressionType type) {
        if (value == null) {
            return nullResult(type);
        }
        switch (type) {
        case BOOLEAN:
            return booleanResult((Boolean) value);
        case DOUBLE:
            return doubleResult((Double) value);
        case FLOAT:
            return floatResult((Float) value);
        case INTEGER:
            return intResult((Integer) value);
        case LONG:
            return longResult((Long) value);
        case STRING:
            return stringResult((String) value);
        default:
            throw new UnsupportedOperationException();
        }
    }

    private static class StringExpressionResult extends BaseExpressionResult {

        private final String value;

        public StringExpressionResult(String value) {
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
        protected boolean valueEquals(BaseExpressionResult other) {
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
            StringExpressionResult other = (StringExpressionResult) obj;
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

    private static abstract class BaseExpressionResult implements ExpressionResult {

        private final ExpressionType type;

        public BaseExpressionResult(ExpressionType type) {
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
        public ExpressionType getType() {
            return type;
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
            BaseExpressionResult other = (BaseExpressionResult) obj;
            if (type != other.type) {
                return false;
            }
            if (isNull() != other.isNull()) {
                return false;
            }
            return valueEquals(other);
        }

        protected abstract boolean valueEquals(BaseExpressionResult other);

        protected abstract int valueHashCode();
    }

    private ExpressionResultFactory() {
        throw new UnsupportedOperationException();
    }
}
