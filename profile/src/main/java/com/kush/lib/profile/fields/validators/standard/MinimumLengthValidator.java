package com.kush.lib.profile.fields.validators.standard;

import com.kush.utils.exceptions.ValidationFailedException;

public class MinimumLengthValidator extends BaseValidator<String> {

    private final int minimumLength;

    public MinimumLengthValidator(int minimumLength) {
        this.minimumLength = minimumLength;
    }

    @Override
    protected void validateAdapted(String value) throws ValidationFailedException {
        if (value.length() < minimumLength) {
            throw new ValidationFailedException("Minimum allowed length is %d", minimumLength);
        }
    }
}
