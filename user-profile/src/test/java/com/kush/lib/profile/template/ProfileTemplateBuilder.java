package com.kush.lib.profile.template;

import java.util.ArrayList;
import java.util.List;

import com.kush.lib.profile.fields.Field;

public class ProfileTemplateBuilder {

    private final List<Field<?>> fields = new ArrayList<>();

    public static ProfileTemplateBuilder create() {
        return new ProfileTemplateBuilder();
    }

    public ProfileTemplate build() {
        return new ProfileTemplate();
    }

    public ProfileTemplateBuilder withField(Field<?> field) {
        fields.add(field);
        return this;
    }
}
