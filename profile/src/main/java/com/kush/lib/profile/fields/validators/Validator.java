package com.kush.lib.profile.fields.validators;

public interface Validator {

    void validate(Object value) throws ValidationFailedException;
}
