package com.kush.lib.profile.template;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kush.lib.profile.fields.Field;

public class ProfileTemplateBuilder {

    private final List<Field> fields = new ArrayList<>();

    public static ProfileTemplateBuilder create() {
        return new ProfileTemplateBuilder();
    }

    public ProfileTemplateBuilder withField(Field field) {
        fields.add(field);
        return this;
    }

    public ProfileTemplate build() {
        Map<String, Field> fieldsAsMap = fields.stream().collect(toMap(f -> f.getName(), identity()));
        return new ProfileTemplate(fieldsAsMap);
    }
}
