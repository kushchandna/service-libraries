package com.kush.lib.group.persistors;

import java.time.LocalDateTime;
import java.util.List;

import com.kush.commons.id.Identifier;
import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistenceOperationFailedException;

public interface GroupPersister extends Persister<Group> {

    Group createGroup(String groupName, Identifier owner, LocalDateTime createdAt) throws PersistenceOperationFailedException;

    Group getGroup(Identifier groupId) throws PersistenceOperationFailedException;

    GroupMembership addGroupMember(Identifier groupId, Identifier member, LocalDateTime addedAt)
            throws PersistenceOperationFailedException;

    List<GroupMembership> getGroupMembers(Identifier groupId) throws PersistenceOperationFailedException;

    List<Group> getGroups(Identifier memberUserId) throws PersistenceOperationFailedException;

    boolean removeGroup(Identifier groupId) throws PersistenceOperationFailedException;

	boolean isMember(Identifier currentUserId, Identifier toGroupId);
}
