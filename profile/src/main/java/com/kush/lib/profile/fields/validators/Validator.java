package com.kush.lib.profile.fields.validators;

import com.kush.utils.exceptions.ValidationFailedException;

public interface Validator {

    void validate(Object value) throws ValidationFailedException;
}
