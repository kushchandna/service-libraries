package com.kush.lib.auth.authorization;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.Arrays;
import java.util.Objects;

public class Permission {

    private final String[] keys;

    public Permission(String... keys) {
        validateKeys(keys);
        this.keys = Arrays.copyOf(keys, keys.length);
    }

    @Override
    public String toString() {
        return Arrays.toString(keys);
    }

    private static void validateKeys(String... keys) {
        Objects.requireNonNull(keys, "keys");
        if (keys.length == 0) {
            throw new IllegalArgumentException("Keys can not be empty");
        }
        for (String key : keys) {
            if (isNullOrEmpty(key)) {
                throw new IllegalArgumentException("No member of key can be empty");
            }
        }
    }
}
