package com.kush.lib.userprofile.persistor;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.userprofile.profile.UserProfile;

public class DefaultUserProfilePersistor extends DelegatingPersistor<UserProfile> implements UserProfilePersistor {

    public DefaultUserProfilePersistor(Persistor<UserProfile> delegate) {
        super(delegate);
    }

    @Override
    public UserProfile getProfileForUser(User user) throws PersistorOperationFailedException {
        return null;
    }
}
