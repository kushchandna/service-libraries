package com.kush.lib.group.persistors;

import java.time.LocalDateTime;
import java.util.List;

import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public interface GroupPersistor extends Persistor<Group> {

    Group createGroup(String groupName, Identifier owner, LocalDateTime createdAt) throws PersistorOperationFailedException;

    Group getGroup(Identifier groupId) throws PersistorOperationFailedException;

    GroupMembership addGroupMember(Identifier groupId, Identifier member, LocalDateTime addedAt)
            throws PersistorOperationFailedException;

    List<GroupMembership> getGroupMembers(Identifier groupId) throws PersistorOperationFailedException;
}
