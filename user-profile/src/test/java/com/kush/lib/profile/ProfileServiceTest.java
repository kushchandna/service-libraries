package com.kush.lib.profile;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.profile.persistors.DefaultProfilePersistor;
import com.kush.lib.profile.persistors.ProfilePersistor;
import com.kush.lib.profile.profile.Profile;
import com.kush.lib.profile.services.ProfileService;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.lib.service.server.TestApplicationEnvironment;

public class ProfileServiceTest {

    @Rule
    public TestApplicationEnvironment testEnv = new TestApplicationEnvironment() {

        @Override
        protected ContextBuilder createContextBuilder() {
            Persistor<Profile> delegate = InMemoryPersistor.forType(Profile.class);
            return ContextBuilder.create()
                .withInstance(ProfilePersistor.class, new DefaultProfilePersistor(delegate));
        };
    };

    private ProfileService userProfileService;

    @Before
    public void beforeEachTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        userProfileService = new ProfileService();
        testEnv.registerService(userProfileService);
    }

    @Test
    public void addBasicUserProfileFields() throws Exception {
        testEnv.runAuthenticatedOperation(() -> {
        });
    }
}
