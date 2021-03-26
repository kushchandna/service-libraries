package com.kush.lib.collections.utils;

import java.util.Optional;

public final class NullableOptional<T> {

    private final T value;
    private final boolean defined;

    private NullableOptional(T value) {
        this.value = value;
        defined = true;
    }

    private NullableOptional() {
        value = null;
        defined = false;
    }

    public static <T> NullableOptional<T> of(T value) {
        return new NullableOptional<>(value);
    }

    public static <T> NullableOptional<T> empty() {
        return new NullableOptional<>();
    }

    public T get() {
        return value;
    }

    public boolean isPresent() {
        return defined;
    }

    public boolean isAbsent() {
        return !defined;
    }

    public T orElse(T defaultValue) {
        return defined ? value : defaultValue;
    }

    public static <T> Optional<T> toJavaOptional(NullableOptional<T> nullableOptional) {
        return nullableOptional.isPresent() ? Optional.ofNullable(nullableOptional.get()) : Optional.empty();
    }

    public static <T> NullableOptional<T> fromJavaOptional(Optional<T> optional) {
        return optional.isPresent() ? NullableOptional.of(optional.get()) : NullableOptional.empty();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (defined ? 1231 : 1237);
        result = prime * result + (value == null ? 0 : value.hashCode());
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
        NullableOptional<?> other = (NullableOptional<?>) obj;
        if (defined != other.defined) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return defined ? "NullableOptional [value = " + value + "]" : "NullableOptional [<empty>]";
    }
}
