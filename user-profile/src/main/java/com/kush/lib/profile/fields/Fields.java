package com.kush.lib.profile.fields;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Fields {

    public static FieldBuilder<String> createTextFieldBuilder() {
        return new FieldBuilder<>();
    }

    public static FieldBuilder<Integer> createIntegerFieldBuilder() {
        return new FieldBuilder<>();
    }

    public static FieldBuilder<Double> createNumericFieldBuilder() {
        return new FieldBuilder<>();
    }

    public static FieldBuilder<Boolean> createBooleanFieldBuilder() {
        return new FieldBuilder<>();
    }

    public static FieldBuilder<LocalDate> createDateFieldBuilder() {
        return new FieldBuilder<>();
    }

    public static FieldBuilder<LocalDateTime> createDateTimeFieldBuilder() {
        return new FieldBuilder<>();
    }
}
