package com.kush.lib.profile.persistors;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.profile.entities.Profile;
import com.kush.lib.service.remoting.auth.User;
import com.kush.utils.id.Identifier;

public interface ProfilePersistor extends Persistor<Profile> {

    Profile getProfile(User owner) throws PersistorOperationFailedException;

    Profile createProfile(User owner) throws PersistorOperationFailedException;

    void updateProfileField(Identifier profileId, String fieldName, Object value) throws PersistorOperationFailedException;

    List<Profile> getMatchingProfiles(Map<String, Set<Object>> fieldVsValues) throws PersistorOperationFailedException;
}
