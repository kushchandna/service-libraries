package com.kush.lib.profile.fields;

import com.kush.lib.profile.fields.validators.Validator;

public interface Field<T> {

    String getName();

    Validator<T> getValidator();
}
