package com.kush.lib.profile.fields.validators.standard;

import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.fields.validators.Validator;

public class MaximumLengthValidator implements Validator<String> {

    private final int maximumLength;

    public MaximumLengthValidator(int maximumLength) {
        this.maximumLength = maximumLength;
    }

    @Override
    public void validate(String value) throws ValidationFailedException {
        if (value.length() > maximumLength) {
            throw new ValidationFailedException("Length exceeded maximum allowed %d", maximumLength);
        }
    }
}
