package com.kush.lib.group.entities;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kush.commons.id.Identifier;
import com.kush.lib.group.persistors.GroupPersister;
import com.kush.lib.persistence.api.DelegatingPersister;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistenceOperationFailedException;

public class DefaultGroupPersistor extends DelegatingPersister<Group> implements GroupPersister {

    private final Persister<GroupMembership> groupMembershipPersistor;

    public DefaultGroupPersistor(Persister<Group> delegate, Persister<GroupMembership> groupMembershipPersistor) {
        super(delegate);
        this.groupMembershipPersistor = groupMembershipPersistor;
    }

    @Override
    public Group createGroup(String groupName, Identifier owner, LocalDateTime createdAt)
            throws PersistenceOperationFailedException {
        Group groupToSave = new Group(groupName, owner, createdAt);
        return save(groupToSave);
    }

    @Override
    public Group getGroup(Identifier groupId) throws PersistenceOperationFailedException {
        return fetch(groupId);
    }

    @Override
    public GroupMembership addGroupMember(Identifier groupId, Identifier member, LocalDateTime addedAt)
            throws PersistenceOperationFailedException {
        List<GroupMembership> matchingMemberships =
                groupMembershipPersistor.fetch(mem -> mem.getGroupId().equals(groupId) && mem.getMember().equals(member));
        if (!matchingMemberships.isEmpty()) {
            throw new PersistenceOperationFailedException("User is already a member");
        }
        GroupMembership membership = new GroupMembership(groupId, member, addedAt);
        return groupMembershipPersistor.save(membership);
    }

    @Override
    public List<GroupMembership> getGroupMembers(Identifier groupId) throws PersistenceOperationFailedException {
        return groupMembershipPersistor.fetch(m -> m.getGroupId().equals(groupId));
    }

    @Override
    public List<Group> getGroups(Identifier memberUserId) throws PersistenceOperationFailedException {
        List<GroupMembership> selfMemberships = groupMembershipPersistor.fetch(gm -> gm.getMember().equals(memberUserId));
        List<Group> groups = new ArrayList<>();
        for (GroupMembership membership : selfMemberships) {
            Group group = getGroup(membership.getGroupId());
            groups.add(group);
        }
        return groups;
    }

    @Override
    public boolean removeGroup(Identifier groupId) throws PersistenceOperationFailedException {
        removeAllGroupMembers(groupId);
        return remove(groupId);
    }

    private void removeAllGroupMembers(Identifier groupId) throws PersistenceOperationFailedException {
        List<GroupMembership> groupMembers = getGroupMembers(groupId);
        List<Identifier> membershipIds = groupMembers.stream().map(GroupMembership::getId).collect(toList());
        for (Identifier memId : membershipIds) {
            if (!groupMembershipPersistor.remove(memId)) {
                throw new PersistenceOperationFailedException("Unable to remove membership with id " + memId);
            }
        }
    }
}
