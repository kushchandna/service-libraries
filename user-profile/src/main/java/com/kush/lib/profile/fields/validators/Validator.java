package com.kush.lib.profile.fields.validators;

public interface Validator<T> {

    void validate(T value) throws ValidationFailedException;
}
