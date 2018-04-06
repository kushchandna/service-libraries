package com.kush.lib.profile.fields;

import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.fields.validators.Validator;

public class ValueValidator {

    public <T> void validate(Field<T> field, T value) throws ValidationFailedException {
        Validator<T> validator = field.getValidator();
        if (validator != null) {
            validator.validate(value);
        }
    }
}
