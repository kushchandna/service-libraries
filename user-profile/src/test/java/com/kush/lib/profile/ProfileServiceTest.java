package com.kush.lib.profile;

import org.junit.Before;
import org.junit.Test;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.profile.entities.DefaultProfilePersistor;
import com.kush.lib.profile.entities.Profile;
import com.kush.lib.profile.fields.Field;
import com.kush.lib.profile.fields.Fields;
import com.kush.lib.profile.fields.ValueValidator;
import com.kush.lib.profile.fields.validators.standard.EmailValidator;
import com.kush.lib.profile.persistors.ProfilePersistor;
import com.kush.lib.profile.services.UserProfileService;
import com.kush.lib.profile.template.ProfileTemplate;
import com.kush.lib.profile.template.ProfileTemplateBuilder;
import com.kush.lib.service.server.BaseServiceTest;

public class ProfileServiceTest extends BaseServiceTest {

    private static final String FIELD_EMAIL = "emailField";

    private UserProfileService profileService;

    @Before
    public void beforeEachTest() throws Exception {
        profileService = new UserProfileService();
        registerService(profileService);
        setupProfilePersistor();
        setupProfileService();
    }

    @Test
    public void addBasicUserProfileFields() throws Exception {
        runAuthenticatedOperation(() -> {
            profileService.updateProfileField(FIELD_EMAIL, "testuser@domain.com");
            Profile profile = profileService.getProfile();
            System.out.println(profile);
        });
    }

    private void setupProfilePersistor() {
        Persistor<Profile> delegate = InMemoryPersistor.forType(Profile.class);
        addToContext(ProfilePersistor.class, new DefaultProfilePersistor(delegate));
    }

    private void setupProfileService() {
        addToContext(ProfileTemplate.class, createProfileTemplate());
        addToContext(ValueValidator.class, new ValueValidator());
    }

    private ProfileTemplate createProfileTemplate() {
        Field emailField = Fields.createTextFieldBuilder(FIELD_EMAIL)
            .addValidator(new EmailValidator())
            .build();
        return ProfileTemplateBuilder.create()
            .withField(emailField)
            .build();
    }
}
