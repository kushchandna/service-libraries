package com.kush.lib.group.persistors;

import java.time.LocalDateTime;

import com.kush.lib.group.entities.Group;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public interface GroupPersistor extends Persistor<Group> {

    Group createGroup(String groupName, Identifier owner, LocalDateTime createdAt) throws PersistorOperationFailedException;
}
