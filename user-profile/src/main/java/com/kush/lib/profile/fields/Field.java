package com.kush.lib.profile.fields;

import com.kush.lib.profile.fields.validators.Validator;

public interface Field {

    String getName();

    Validator getValidator();
}
