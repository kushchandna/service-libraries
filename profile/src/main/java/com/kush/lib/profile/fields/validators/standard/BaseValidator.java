package com.kush.lib.profile.fields.validators.standard;

import com.kush.lib.profile.fields.validators.Validator;
import com.kush.utils.exceptions.ValidationFailedException;

public abstract class BaseValidator<T> implements Validator {

    @Override
    @SuppressWarnings("unchecked")
    public final void validate(Object value) throws ValidationFailedException {
        validateAdapted((T) value);
    }

    protected abstract void validateAdapted(T value) throws ValidationFailedException;
}
