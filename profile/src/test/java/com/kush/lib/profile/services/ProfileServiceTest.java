package com.kush.lib.profile.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.profile.entities.DefaultProfilePersistor;
import com.kush.lib.profile.entities.Profile;
import com.kush.lib.profile.fields.Field;
import com.kush.lib.profile.fields.Fields;
import com.kush.lib.profile.fields.ValueValidator;
import com.kush.lib.profile.fields.validators.standard.EmailValidator;
import com.kush.lib.profile.fields.validators.standard.PhoneNumberValidator;
import com.kush.lib.profile.persistors.ProfilePersistor;
import com.kush.lib.profile.template.ProfileTemplate;
import com.kush.lib.profile.template.ProfileTemplateBuilder;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseServiceTest;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.id.Identifier;

public class ProfileServiceTest extends BaseServiceTest {

    private static final String FIELD_EMAIL = "emailId";
    private static final String FIELD_NAME = "fullName";
    private static final String FIELD_PHONE = "phone";

    @Rule
    public ExpectedException expected = ExpectedException.none();

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

    @Test
    public void invalidEmailId() throws Exception {
        runAuthenticatedOperation(() -> {
            expected.expect(ValidationFailedException.class);
            expected.expectMessage(""
                    + "Invalid value specified for field Email Id. "
                    + "Reason: 'invalid_email_id' is not a valid email id");
            profileService.updateProfileField(FIELD_EMAIL, "invalid_email_id");
        });
    }

    @Test
    public void addRepeatedValueWhenAllowed() throws Exception {
        User user1 = getUser(0);
        runAuthenticatedOperation(user1, () -> {
            profileService.updateProfileField(FIELD_NAME, "Test User");
        });

        User user2 = getUser(1);
        runAuthenticatedOperation(user2, () -> {
            profileService.updateProfileField(FIELD_NAME, "Test User");
        });

        User user3 = getUser(2);
        runAuthenticatedOperation(user3, () -> {
            Iterator<Identifier> matchingUsers = profileService.findMatchingUsers(FIELD_NAME, "Test User");
            assertThat(matchingUsers.next(), is(equalTo(user1.getId())));
            assertThat(matchingUsers.next(), is(equalTo(user2.getId())));
            assertThat(matchingUsers.hasNext(), is(equalTo(false)));
        });
    }

    @Test
    public void addRepeatedValueWhenNotAllowed() throws Exception {
        User user1 = getUser(0);
        runAuthenticatedOperation(user1, () -> {
            profileService.updateProfileField(FIELD_EMAIL, "testuser@domain.com");
        });

        User user2 = getUser(1);
        runAuthenticatedOperation(user2, () -> {
            expected.expect(ValidationFailedException.class);
            expected.expectMessage("User with Email Id 'testuser@domain.com' already exists.");
            profileService.updateProfileField(FIELD_EMAIL, "testuser@domain.com");
        });
    }

    @Test
    public void saveNonRepeatablePhoneNumber() throws Exception {
        User user1 = getUser(0);
        runAuthenticatedOperation(user1, () -> {
        });

        User user2 = getUser(1);
        runAuthenticatedOperation(user2, () -> {
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
            .withDisplayName("Email Id")
            .withNoRepeatitionAllowed()
            .build();
        Field nameField = Fields.createTextFieldBuilder(FIELD_NAME)
            .withDisplayName("Name")
            .build();
        Field phoneField = Fields.createTextFieldBuilder(FIELD_PHONE)
            .addValidator(new PhoneNumberValidator())
            .withNoRepeatitionAllowed()
            .build();
        return ProfileTemplateBuilder.create()
            .withField(nameField)
            .withField(emailField)
            .withField(phoneField)
            .build();
    }
}
