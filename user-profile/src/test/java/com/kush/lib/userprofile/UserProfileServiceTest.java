package com.kush.lib.userprofile;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.lib.service.server.TestApplicationEnvironment;
import com.kush.lib.userprofile.persistor.DefaultUserProfilePersistor;
import com.kush.lib.userprofile.persistor.UserProfilePersistor;
import com.kush.lib.userprofile.profile.UserProfile;
import com.kush.lib.userprofile.service.UserProfileService;

public class UserProfileServiceTest {

    @Rule
    public TestApplicationEnvironment testEnv = new TestApplicationEnvironment() {

        @Override
        protected ContextBuilder createContextBuilder() {
            Persistor<UserProfile> delegate = InMemoryPersistor.forType(UserProfile.class);
            return ContextBuilder.create()
                .withInstance(UserProfilePersistor.class, new DefaultUserProfilePersistor(delegate));
        };
    };

    private UserProfileService userProfileService;

    @Before
    public void beforeEachTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        userProfileService = new UserProfileService();
        testEnv.registerService(userProfileService);
    }

    @Test
    public void addBasicUserProfileFields() throws Exception {
        testEnv.runAuthenticatedOperation(() -> {
        });
    }
}
