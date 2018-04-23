package com.kush.lib.profile.fields.validators.standard;

import com.kush.lib.profile.fields.validators.ValidationFailedException;

public class MinimumValueValidator<T extends Comparable<T>> extends BaseValidator<T> {

    private final T minimumValue;

    public MinimumValueValidator(T minimumValue) {
        this.minimumValue = minimumValue;
    }

    @Override
    protected void validateAdapted(T value) throws ValidationFailedException {
        if (value.compareTo(minimumValue) < 0) {
            throw new ValidationFailedException("Given value [%s] is less than minimum allowed [%s]", value, minimumValue);
        }
    }
}
