package com.kush.lib.profile.fields.validators.standard;

import com.kush.lib.profile.fields.validators.ValidationFailedException;

public class MaximumValueValidator<T extends Comparable<T>> extends BaseValidator<T> {

    private final T maximumValue;

    public MaximumValueValidator(T maximumValue) {
        this.maximumValue = maximumValue;
    }

    @Override
    protected void validateAdapted(T value) throws ValidationFailedException {
        if (value.compareTo(maximumValue) > 0) {
            throw new ValidationFailedException("Given value [%s] exceeds maximum allowed [%s]", value, maximumValue);
        }
    }
}
