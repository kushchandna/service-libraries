package com.kush.lib.profile.fields.validators.standard;

import com.kush.lib.profile.fields.validators.ValidationFailedException;

public class MaximumLengthValidator extends BaseValidator<String> {

    private final int maximumLength;

    public MaximumLengthValidator(int maximumLength) {
        this.maximumLength = maximumLength;
    }

    @Override
    protected void validateAdapted(String value) throws ValidationFailedException {
        if (value.length() > maximumLength) {
            throw new ValidationFailedException("Length exceeded maximum allowed %d", maximumLength);
        }
    }
}
