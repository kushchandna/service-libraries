package com.kush.lib.profile.persistors;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.profile.entities.Profile;

public class DefaultProfilePersistor extends DelegatingPersistor<Profile> implements ProfilePersistor {

    public DefaultProfilePersistor(Persistor<Profile> delegate) {
        super(delegate);
    }
}
