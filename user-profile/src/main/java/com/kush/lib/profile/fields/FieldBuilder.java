package com.kush.lib.profile.fields;

import com.kush.lib.profile.fields.validators.Validator;

public class FieldBuilder<T> {

    private final ComboValidator<T> comboValidator = new ComboValidator<>();

    private final String name;

    public FieldBuilder(String name) {
        this.name = name;
    }

    public FieldBuilder<T> addValidator(Validator<T> validator) {
        comboValidator.addValidator(validator);
        return this;
    }

    public Field<T> build() {
        return new Field<T>() {

            @Override
            public String getName() {
                return name;
            }

            @Override
            public Validator<T> getValidator() {
                return comboValidator;
            }
        };
    }
}
