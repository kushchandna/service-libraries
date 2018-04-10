package com.kush.lib.profile.fields.validators.standard;

import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.fields.validators.Validator;

public class MinimumValueValidator<T extends Comparable<T>> implements Validator<T> {

    private final T minimumValue;

    public MinimumValueValidator(T minimumValue) {
        this.minimumValue = minimumValue;
    }

    @Override
    public void validate(T value) throws ValidationFailedException {
        if (value.compareTo(minimumValue) < 0) {
            throw new ValidationFailedException("Given value [%s] is less than minimum allowed [%s]", value, minimumValue);
        }
    }
}
