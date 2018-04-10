package com.kush.lib.profile.fields.validators.standard;

import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.fields.validators.Validator;

public class RangeValidator<N extends Number> implements Validator<N> {

    private final N minimum;
    private final N maximum;

    public RangeValidator(N minimum, N maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public void validate(N value) throws ValidationFailedException {
        if (value.doubleValue() < minimum.doubleValue() || value.doubleValue() > maximum.doubleValue()) {
            throw new ValidationFailedException("Only values in range %s and %s are allowed", minimum, maximum);
        }
    }
}
