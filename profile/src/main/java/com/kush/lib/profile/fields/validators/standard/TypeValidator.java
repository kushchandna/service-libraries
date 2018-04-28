package com.kush.lib.profile.fields.validators.standard;

import com.kush.lib.profile.fields.validators.Validator;
import com.kush.utils.exceptions.ValidationFailedException;

public class TypeValidator<T> implements Validator {

    private final Class<T> type;

    public static <T> TypeValidator<T> forType(Class<T> type) {
        return new TypeValidator<>(type);
    }

    private TypeValidator(Class<T> type) {
        this.type = type;
    }

    @Override
    public void validate(Object value) throws ValidationFailedException {
        if (!type.isAssignableFrom(value.getClass())) {
            throw new ValidationFailedException("Invalid type");
        }
    }
}
