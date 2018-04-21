package com.kush.lib.profile;

import org.junit.Before;
import org.junit.Test;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.profile.entities.Profile;
import com.kush.lib.profile.persistors.DefaultProfilePersistor;
import com.kush.lib.profile.persistors.ProfilePersistor;
import com.kush.lib.profile.services.ProfileService;
import com.kush.lib.service.server.BaseServiceTest;

public class ProfileServiceTest extends BaseServiceTest {

    private ProfileService profileService;

    @Before
    public void beforeEachTest() throws Exception {
        profileService = new ProfileService();
        Persistor<Profile> delegate = InMemoryPersistor.forType(Profile.class);
        addToContext(ProfilePersistor.class, new DefaultProfilePersistor(delegate));
    }

    @Test
    public void addBasicUserProfileFields() throws Exception {
        runAuthenticatedOperation(() -> {
            profileService.toString();
        });
    }
}
