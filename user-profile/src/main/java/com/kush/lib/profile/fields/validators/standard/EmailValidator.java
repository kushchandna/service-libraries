package com.kush.lib.profile.fields.validators.standard;

import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.fields.validators.Validator;

public class EmailValidator implements Validator<String> {

    @Override
    public void validate(String value) throws ValidationFailedException {
        if (!org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(value)) {
            throw new ValidationFailedException("Invalid email id specified %s", value);
        }
    }
}
