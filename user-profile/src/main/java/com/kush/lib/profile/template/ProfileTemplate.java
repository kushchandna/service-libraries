package com.kush.lib.profile.template;

import java.util.HashMap;
import java.util.Map;

import com.kush.lib.profile.fields.Field;

public class ProfileTemplate {

    private final Map<String, Field> fields = new HashMap<>();

    ProfileTemplate(Map<String, Field> fields) {
        this.fields.putAll(fields);
    }

    public Field getField(String fieldName) {
        return fields.get(fieldName);
    }
}
