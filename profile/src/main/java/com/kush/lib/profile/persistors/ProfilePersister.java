package com.kush.lib.profile.persistors;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kush.commons.id.Identifier;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.profile.entities.Profile;
import com.kush.lib.service.remoting.auth.User;

public interface ProfilePersister extends Persister<Profile> {

    Profile getProfile(User owner) throws PersistorOperationFailedException;

    Profile createProfile(User owner) throws PersistorOperationFailedException;

    void updateProfileField(Identifier profileId, String fieldName, Object value) throws PersistorOperationFailedException;

    List<Profile> getMatchingProfiles(Map<String, Set<Object>> fieldVsValues) throws PersistorOperationFailedException;
}
