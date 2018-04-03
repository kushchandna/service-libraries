package com.kush.usergroups;

import java.time.LocalDateTime;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public interface UserGroupPersistor extends Persistor<UserGroup> {

    UserGroup createGroup(String name, Identifier owner, LocalDateTime creationDate) throws PersistorOperationFailedException;

    UserGroup getGroup(Identifier groupId) throws PersistorOperationFailedException;
}
