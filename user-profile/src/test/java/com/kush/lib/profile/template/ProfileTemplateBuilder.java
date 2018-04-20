package com.kush.lib.profile.template;

public class ProfileTemplateBuilder {

    public static ProfileTemplateBuilder create() {
        return new ProfileTemplateBuilder();
    }

    public ProfileTemplate build() {
        return new ProfileTemplate();
    }
}
