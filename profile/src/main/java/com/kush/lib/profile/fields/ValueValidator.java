package com.kush.lib.profile.fields;

import com.kush.lib.profile.fields.validators.Validator;
import com.kush.utils.exceptions.ValidationFailedException;

public class ValueValidator {

    public void validate(Field field, Object value) throws ValidationFailedException {
        Validator validator = field.getValidator();
        if (validator != null) {
            try {
                validator.validate(value);
            } catch (ValidationFailedException e) {
                throw new ValidationFailedException("Invalid value specified for field %s. Reason: %s", field.getDisplayName(),
                        e.getMessage());
            }
        }
    }
}
