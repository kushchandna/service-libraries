package com.kush.lib.profile.fields.validators.standard;

import org.apache.commons.lang3.StringUtils;

import com.kush.utils.exceptions.ValidationFailedException;

public class NonEmptyTextValidator extends BaseValidator<String> {

    @Override
    protected void validateAdapted(String value) throws ValidationFailedException {
        if (StringUtils.isEmpty(value)) {
            throw new ValidationFailedException("Empty or null value not allowed");
        }
    }
}
