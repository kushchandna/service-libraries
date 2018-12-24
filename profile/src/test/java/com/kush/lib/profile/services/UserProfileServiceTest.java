package com.kush.lib.profile.services;

import static com.google.common.collect.Sets.newHashSet;
import static com.kush.utils.commons.CollectionUtils.singletonMultiValueMap;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.kush.lib.profile.fields.validators.standard.EmailValidator;
import com.kush.lib.profile.persistors.ProfilePersistor;
import com.kush.lib.profile.template.ProfileTemplate;
import com.kush.lib.profile.template.ProfileTemplateBuilder;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.BaseServiceTest;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.id.Identifier;

public class UserProfileServiceTest extends BaseServiceTest {

    private static final String FIELD_EMAIL = "emailId";
    private static final String FIELD_NAME = "fullName";
    private static final String FIELD_PHONE = "phone";

    @Rule
    public ExpectedException expected = ExpectedException.none();

    private UserProfileService profileService;

    @Before
    public void beforeEachTest() throws Exception {
        setupProfilePersistor();
        setupProfileService();
        profileService = registerService(UserProfileService.class);
    }

    @Test
    public void addBasicUserProfileFields() throws Exception {
        User user1 = user(0);
        runAuthenticatedOperation(user1, () -> {
            profileService.updateProfileField(FIELD_EMAIL, "testuser@domain.com");
        });

        User user2 = user(1);
        runAuthenticatedOperation(user2, () -> {
            List<Identifier> users = profileService.findMatchingUsers(singletonMultiValueMap(FIELD_EMAIL, "testuser@domain.com"));
            assertThat(users.get(0), is(equalTo(user1.getId())));
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
        User user1 = user(0);
        runAuthenticatedOperation(user1, () -> {
            profileService.updateProfileField(FIELD_NAME, "Test User");
        });

        User user2 = user(1);
        runAuthenticatedOperation(user2, () -> {
            profileService.updateProfileField(FIELD_NAME, "Test User");
        });

        User user3 = user(2);
        runAuthenticatedOperation(user3, () -> {
            List<Identifier> matchingUsers = profileService.findMatchingUsers(singletonMultiValueMap(FIELD_NAME, "Test User"));
            assertThat(matchingUsers, hasSize(2));
            assertThat(matchingUsers.get(0), is(equalTo(user1.getId())));
            assertThat(matchingUsers.get(1), is(equalTo(user2.getId())));
        });
    }

    @Test
    public void addRepeatedValueWhenNotAllowed() throws Exception {
        User user1 = user(0);
        runAuthenticatedOperation(user1, () -> {
            profileService.updateProfileField(FIELD_EMAIL, "testuser@domain.com");
        });

        User user2 = user(1);
        runAuthenticatedOperation(user2, () -> {
            expected.expect(ValidationFailedException.class);
            expected.expectMessage("User with Email Id 'testuser@domain.com' already exists.");
            profileService.updateProfileField(FIELD_EMAIL, "testuser@domain.com");
        });
    }

    @Test
    public void saveNonRepeatablePhoneNumber() throws Exception {
        runAuthenticatedOperation(user(0), () -> {

        });

        runAuthenticatedOperation(user(1), () -> {
        });
    }

    @Test
    public void getMatchingUsersFromContacts() throws Exception {
        User self = user(0);
        User user1 = user(1);
        User user2 = user(2);
        User user3 = user(3);
        User user4 = user(4);

        runAuthenticatedOperation(user1, () -> {
            profileService.updateProfileField(FIELD_EMAIL, "user1@domain.com");
        });
        runAuthenticatedOperation(user2, () -> {
            profileService.updateProfileField(FIELD_PHONE, "111111111");
            profileService.updateProfileField(FIELD_EMAIL, "user2@domain.com");
        });
        runAuthenticatedOperation(user3, () -> {
            profileService.updateProfileField(FIELD_PHONE, "222222222");
        });
        runAuthenticatedOperation(user4, () -> {
            profileService.updateProfileField(FIELD_PHONE, "333333333");
        });
        runAuthenticatedOperation(self, () -> {
            Map<String, Set<Object>> fieldVsValues = new HashMap<>();
            fieldVsValues.put(FIELD_EMAIL, newHashSet("user1@domain.com", "user2@domain.com"));
            fieldVsValues.put(FIELD_PHONE, newHashSet("111111111", "222222222"));
            List<Identifier> matchingUsers = profileService.findMatchingUsers(fieldVsValues);
            assertThat(matchingUsers, containsInAnyOrder(user1.getId(), user2.getId(), user3.getId()));
        });
    }

    private void setupProfilePersistor() {
        Persistor<Profile> delegate = InMemoryPersistor.forType(Profile.class);
        addToContext(ProfilePersistor.class, new DefaultProfilePersistor(delegate));
    }

    private void setupProfileService() {
        addToContext(ProfileTemplate.class, createProfileTemplate());
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
            .withNoRepeatitionAllowed()
            .build();
        return ProfileTemplateBuilder.create()
            .withField(nameField)
            .withField(emailField)
            .withField(phoneField)
            .build();
    }
}
