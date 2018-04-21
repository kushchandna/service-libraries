package com.kush.lib.group.entities;

import java.time.LocalDateTime;

import com.kush.lib.group.persistors.GroupPersistor;
import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public class DefaultGroupPersistor extends DelegatingPersistor<Group> implements GroupPersistor {

    public DefaultGroupPersistor(Persistor<Group> delegate) {
        super(delegate);
    }

    @Override
    public Group createGroup(String groupName, Identifier owner, LocalDateTime createdAt)
            throws PersistorOperationFailedException {
        Group groupToSave = new Group(groupName, owner, createdAt);
        return save(groupToSave);
    }
}
