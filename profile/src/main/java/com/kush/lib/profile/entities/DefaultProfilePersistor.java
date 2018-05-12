package com.kush.lib.profile.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        List<Profile> filteredProfiles = fetch(p -> p.getOwner().equals(owner));
        return filteredProfiles.isEmpty() ? null : filteredProfiles.get(0);
    }

    @Override
    public void updateProfileField(Identifier profileId, String fieldName, Object value)
            throws PersistorOperationFailedException {
        Profile profile = fetch(profileId);
        profile.updateField(fieldName, value);
    }

    @Override
    public List<Profile> getMatchingProfiles(Map<String, Set<Object>> fieldVsValues)
            throws PersistorOperationFailedException {
        List<Profile> allProfiles = fetchAll();
        List<Profile> matchingProfiles = new ArrayList<>();
        for (Profile profile : allProfiles) {
            Map<String, Object> fields = profile.getFields();
            for (String key : fieldVsValues.keySet()) {
                Object savedValue = fields.get(key);
                Set<Object> valuesToMatch = fieldVsValues.get(key);
                if (valuesToMatch.contains(savedValue)) {
                    matchingProfiles.add(profile);
                    break;
                }
            }
        }
        return matchingProfiles;
    }
}
