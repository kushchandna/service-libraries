package com.kush.lib.profile.fields;

import java.util.ArrayList;
import java.util.List;

import com.kush.lib.profile.fields.validators.Validator;
import com.kush.utils.exceptions.ValidationFailedException;

public class ComboValidator implements Validator {

    private final List<Validator> delegates = new ArrayList<>();

    @Override
    public void validate(Object value) throws ValidationFailedException {
        for (Validator validator : delegates) {
            validator.validate(value);
        }
    }

    void addValidator(Validator validator) {
        delegates.add(validator);
    }
}
