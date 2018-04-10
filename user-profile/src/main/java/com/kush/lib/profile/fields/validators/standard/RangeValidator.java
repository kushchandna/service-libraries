package com.kush.lib.profile.fields.validators.standard;

import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.fields.validators.Validator;

public class RangeValidator<T extends Comparable<? super T>> implements Validator<T> {

    private final T minimum;
    private final T maximum;

    public RangeValidator(T minimum, T maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public void validate(T value) throws ValidationFailedException {
        if (value.compareTo(minimum) < 0 || value.compareTo(maximum) > 0) {
            throw new ValidationFailedException("Given value [%s] is not in allowed range of [%s] and [%s]", value, minimum,
                    maximum);
        }
    }
}
