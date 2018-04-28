package com.kush.lib.profile.fields.validators.standard;

import com.kush.utils.exceptions.ValidationFailedException;

public class RangeValidator<T extends Comparable<? super T>> extends BaseValidator<T> {

    private final T minimum;
    private final T maximum;

    public RangeValidator(T minimum, T maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    protected void validateAdapted(T value) throws ValidationFailedException {
        if (value.compareTo(minimum) < 0 || value.compareTo(maximum) > 0) {
            throw new ValidationFailedException("Given value [%s] is not in allowed range of [%s] and [%s]", value, minimum,
                    maximum);
        }
    }
}
