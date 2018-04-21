package com.kush.lib.profile.fields;

import com.kush.lib.profile.fields.validators.Validator;

public class FieldBuilder {

    private final ComboValidator comboValidator = new ComboValidator();

    private final String name;

    public FieldBuilder(String name) {
        this.name = name;
    }

    public FieldBuilder addValidator(Validator validator) {
        comboValidator.addValidator(validator);
        return this;
    }

    public Field build() {
        return new Field() {

            @Override
            public String getName() {
                return name;
            }

            @Override
            public Validator getValidator() {
                return comboValidator;
            }
        };
    }
}
