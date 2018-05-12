package com.kush.lib.profile.persistors;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.profile.entities.Profile;
import com.kush.utils.id.Identifier;

public interface ProfilePersistor extends Persistor<Profile> {

    Profile getProfile(Identifier owner) throws PersistorOperationFailedException;

    Profile createProfile(Identifier owner) throws PersistorOperationFailedException;

    void updateProfileField(Identifier profileId, String fieldName, Object value) throws PersistorOperationFailedException;

    List<Profile> getMatchingProfiles(Map<String, Set<Object>> fieldVsValues) throws PersistorOperationFailedException;
}
