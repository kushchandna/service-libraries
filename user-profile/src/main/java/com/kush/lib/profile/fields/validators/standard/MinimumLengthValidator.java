package com.kush.lib.profile.fields.validators.standard;

import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.fields.validators.Validator;

public class MinimumLengthValidator implements Validator<String> {

    private final int minimumLength;

    public MinimumLengthValidator(int minimumLength) {
        this.minimumLength = minimumLength;
    }

    @Override
    public void validate(String value) throws ValidationFailedException {
        if (value.length() < minimumLength) {
            throw new ValidationFailedException("Minimum allowed length is %d", minimumLength);
        }
    }
}
