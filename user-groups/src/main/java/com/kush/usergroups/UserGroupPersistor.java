package com.kush.usergroups;

import java.time.LocalDateTime;
import java.util.List;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public interface UserGroupPersistor extends Persistor<UserGroup> {

    UserGroup createGroup(String name, Identifier owner, LocalDateTime creationDate) throws PersistorOperationFailedException;

    UserGroup getGroup(Identifier groupId) throws PersistorOperationFailedException;

    UserGroupMember addGroupMember(Identifier groupId, Identifier userId, LocalDateTime memberSince, Identifier addedBy)
            throws PersistorOperationFailedException;

    List<UserGroupMember> getGroupMembers(Identifier groupId) throws PersistorOperationFailedException;
}
