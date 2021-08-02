package com.kush.lib.profile.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kush.commons.id.Identifier;
import com.kush.lib.persistence.api.DelegatingPersister;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.profile.persistors.ProfilePersister;
import com.kush.lib.service.remoting.auth.User;

public class DefaultProfilePersistor extends DelegatingPersister<Profile> implements ProfilePersister {

    public DefaultProfilePersistor(Persister<Profile> delegate) {
        super(delegate);
    }

    @Override
    public Profile createProfile(User owner) throws PersistorOperationFailedException {
        Profile profile = new Profile(owner, Collections.emptyMap());
        return save(profile);
    }

    @Override
    public Profile getProfile(User owner) throws PersistorOperationFailedException {
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
