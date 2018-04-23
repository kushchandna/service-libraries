package com.kush.lib.profile.fields;

import com.kush.lib.profile.fields.validators.Validator;

public interface Field {

    String getName();

    String getDisplayName();

    boolean isNoRepeatitionAllowed();

    Validator getValidator();
}
