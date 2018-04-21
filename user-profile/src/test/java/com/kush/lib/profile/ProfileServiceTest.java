package com.kush.lib.profile;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

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
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseServiceTest;
import com.kush.utils.id.Identifier;

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
        User user1 = getUser(0);
        runAuthenticatedOperation(user1, () -> {
            profileService.updateProfileField(FIELD_EMAIL, "testuser@domain.com");
        });

        User user2 = getUser(1);
        runAuthenticatedOperation(user2, () -> {
            Iterator<Identifier> users = profileService.findMatchingUsers(FIELD_EMAIL, "testuser@domain.com");
            assertThat(users.next(), is(equalTo(user1.getId())));
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
