package com.kush.lib.profile.fields;

import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.fields.validators.Validator;

public class ValueValidator {

    public void validate(Field field, Object value) throws ValidationFailedException {
        Validator validator = field.getValidator();
        if (validator != null) {
            validator.validate(value);
        }
    }
}
