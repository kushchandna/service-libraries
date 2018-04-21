package com.kush.lib.profile.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.profile.persistors.ProfilePersistor;
import com.kush.utils.id.Identifier;

public class DefaultProfilePersistor extends DelegatingPersistor<Profile> implements ProfilePersistor {

    public DefaultProfilePersistor(Persistor<Profile> delegate) {
        super(delegate);
    }

    @Override
    public Profile createProfile(Identifier owner) throws PersistorOperationFailedException {
        Profile profile = new Profile(owner, Collections.emptyMap());
        return save(profile);
    }

    @Override
    public Profile getProfile(Identifier owner) throws PersistorOperationFailedException {
        Iterator<Profile> filteredProfiles = fetch(p -> p.getOwner().equals(owner));
        return filteredProfiles.hasNext() ? filteredProfiles.next() : null;
    }

    @Override
    public void updateProfileField(Identifier profileId, String fieldName, Object value)
            throws PersistorOperationFailedException {
        Profile profile = fetch(profileId);
        profile.updateField(fieldName, value);
    }

    @Override
    public Iterator<Profile> getMatchingProfiles(String fieldName, Object value) throws PersistorOperationFailedException {
        Iterator<Profile> allProfiles = fetchAll();
        List<Profile> matchingProfiles = new ArrayList<>();
        while (allProfiles.hasNext()) {
            Profile profile = allProfiles.next();
            Map<String, Object> fields = profile.getFields();
            Object savedValue = fields.get(fieldName);
            if (value.equals(savedValue)) {
                matchingProfiles.add(profile);
            }
        }
        return matchingProfiles.iterator();
    }
}
