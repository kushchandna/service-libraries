package com.kush.usergroups;

import java.time.LocalDateTime;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public class DefaultUserGroupPersistor extends DelegatingPersistor<UserGroup> implements UserGroupPersistor {

    public DefaultUserGroupPersistor(Persistor<UserGroup> delegate) {
        super(delegate);
    }

    @Override
    public UserGroup createGroup(String name, Identifier owner, LocalDateTime creationDate)
            throws PersistorOperationFailedException {
        UserGroup group = new UserGroup(name, owner, creationDate);
        return save(group);
    }

    @Override
    public UserGroup getGroup(Identifier groupId) throws PersistorOperationFailedException {
        return fetch(groupId);
    }
}
