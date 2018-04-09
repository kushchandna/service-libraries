package com.kush.lib.profile.fields.validators.standard;

import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.fields.validators.Validator;

public class MaximumValueValidator<N extends Number> implements Validator<N> {

    private final N maximumValue;

    public MaximumValueValidator(N maximumValue) {
        this.maximumValue = maximumValue;
    }

    @Override
    public void validate(N value) throws ValidationFailedException {
        if (value.doubleValue() > maximumValue.doubleValue()) {
            throw new ValidationFailedException("Only values less than %s are allowed", maximumValue);
        }
    }
}
