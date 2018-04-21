package com.kush.lib.profile.template;

import org.junit.Test;

import com.kush.lib.profile.fields.Field;
import com.kush.lib.profile.fields.Fields;
import com.kush.lib.profile.fields.validators.standard.EmailValidator;

public class ProfileTemplateTest {

    @Test
    public void testName() throws Exception {
        Field<String> emailField = Fields.createTextFieldBuilder("emailField")
            .addValidator(new EmailValidator())
            .build();
        ProfileTemplate profileTemplate = ProfileTemplateBuilder.create()
            .withField(emailField)
            .build();
        profileTemplate.toString();
    }
}
