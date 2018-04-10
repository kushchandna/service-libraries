package com.kush.lib.profile.fields;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Fields {

    public static FieldBuilder<String> createTextFieldBuilder() {
        return createFieldBuilder();
    }

    public static FieldBuilder<Integer> createIntegerFieldBuilder() {
        return createFieldBuilder();
    }

    public static FieldBuilder<Double> createNumericFieldBuilder() {
        return createFieldBuilder();
    }

    public static FieldBuilder<Boolean> createBooleanFieldBuilder() {
        return createFieldBuilder();
    }

    public static FieldBuilder<LocalDate> createDateFieldBuilder() {
        return createFieldBuilder();
    }

    public static FieldBuilder<LocalDateTime> createDateTimeFieldBuilder() {
        return createFieldBuilder();
    }

    private static <T> FieldBuilder<T> createFieldBuilder() {
        return new FieldBuilder<>();
    }
}
