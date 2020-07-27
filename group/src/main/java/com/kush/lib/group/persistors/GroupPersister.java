package com.kush.lib.group.persistors;

import java.time.LocalDateTime;
import java.util.List;

import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public interface GroupPersister extends Persister<Group> {

    Group createGroup(String groupName, Identifier owner, LocalDateTime createdAt) throws PersistorOperationFailedException;

    Group getGroup(Identifier groupId) throws PersistorOperationFailedException;

    GroupMembership addGroupMember(Identifier groupId, Identifier member, LocalDateTime addedAt)
            throws PersistorOperationFailedException;

    List<GroupMembership> getGroupMembers(Identifier groupId) throws PersistorOperationFailedException;

    List<Group> getGroups(Identifier memberUserId) throws PersistorOperationFailedException;

    boolean removeGroup(Identifier groupId) throws PersistorOperationFailedException;
}
