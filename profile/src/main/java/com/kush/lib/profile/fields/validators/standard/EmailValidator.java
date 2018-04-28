package com.kush.lib.profile.fields.validators.standard;

import com.kush.utils.exceptions.ValidationFailedException;

public class EmailValidator extends BaseValidator<String> {

    @Override
    protected void validateAdapted(String value) throws ValidationFailedException {
        if (!org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(value)) {
            throw new ValidationFailedException("'%s' is not a valid email id", value);
        }
    }
}
