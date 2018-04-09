package com.kush.lib.profile.fields.validators.standard;

import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.fields.validators.Validator;

public class MinimumValueValidator<N extends Number> implements Validator<N> {

    private final N minimumValue;

    public MinimumValueValidator(N minimumValue) {
        this.minimumValue = minimumValue;
    }

    @Override
    public void validate(N value) throws ValidationFailedException {
        if (value.doubleValue() < minimumValue.doubleValue()) {
            throw new ValidationFailedException("Only values greater than %d are allowed", minimumValue);
        }
    }
}
