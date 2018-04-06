package com.kush.lib.profile.fields;

import java.util.ArrayList;
import java.util.List;

import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.fields.validators.Validator;

public class ComboValidator<T> implements Validator<T> {

    private final List<Validator<T>> delegates = new ArrayList<>();

    @Override
    public void validate(T value) throws ValidationFailedException {
        for (Validator<T> validator : delegates) {
            validator.validate(value);
        }
    }

    void addValidator(Validator<T> validator) {
        delegates.add(validator);
    }
}
