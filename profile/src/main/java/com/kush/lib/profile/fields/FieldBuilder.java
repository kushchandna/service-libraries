package com.kush.lib.profile.fields;

import com.kush.lib.profile.fields.validators.Validator;

public class FieldBuilder {

    private final ComboValidator comboValidator = new ComboValidator();

    private final String name;

    private boolean noRepeatitionAllowed = false;
    private String displayName;

    public FieldBuilder(String name) {
        this.name = name;
    }

    public FieldBuilder addValidator(Validator validator) {
        comboValidator.addValidator(validator);
        return this;
    }

    public FieldBuilder withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public FieldBuilder withNoRepeatitionAllowed() {
        noRepeatitionAllowed = true;
        return this;
    }

    public Field build() {
        return new Field() {

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getDisplayName() {
                return displayName == null ? name : displayName;
            }

            @Override
            public boolean isNoRepeatitionAllowed() {
                return noRepeatitionAllowed;
            }

            @Override
            public Validator getValidator() {
                return comboValidator;
            }
        };
    }
}
