package com.kush.lib.group.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kush.lib.group.persistors.GroupPersistor;
import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public class DefaultGroupPersistor extends DelegatingPersistor<Group> implements GroupPersistor {

    private final Persistor<GroupMembership> groupMembershipPersistor;

    public DefaultGroupPersistor(Persistor<Group> delegate, Persistor<GroupMembership> groupMembershipPersistor) {
        super(delegate);
        this.groupMembershipPersistor = groupMembershipPersistor;
    }

    @Override
    public Group createGroup(String groupName, Identifier owner, LocalDateTime createdAt)
            throws PersistorOperationFailedException {
        Group groupToSave = new Group(groupName, owner, createdAt);
        return save(groupToSave);
    }

    @Override
    public Group getGroup(Identifier groupId) throws PersistorOperationFailedException {
        return fetch(groupId);
    }

    @Override
    public GroupMembership addGroupMember(Identifier groupId, Identifier member, LocalDateTime addedAt)
            throws PersistorOperationFailedException {
        List<GroupMembership> matchingMemberships =
                groupMembershipPersistor.fetch(mem -> mem.getGroupId().equals(groupId) && mem.getMember().equals(member));
        if (!matchingMemberships.isEmpty()) {
            throw new PersistorOperationFailedException("User is already a member");
        }
        GroupMembership membership = new GroupMembership(groupId, member, addedAt);
        return groupMembershipPersistor.save(membership);
    }

    @Override
    public List<GroupMembership> getGroupMembers(Identifier groupId) throws PersistorOperationFailedException {
        return groupMembershipPersistor.fetch(m -> m.getGroupId().equals(groupId));
    }

    @Override
    public List<Group> getGroups(Identifier memberUserId) throws PersistorOperationFailedException {
        List<GroupMembership> selfMemberships = groupMembershipPersistor.fetch(gm -> gm.getMember().equals(memberUserId));
        List<Group> groups = new ArrayList<>();
        for (GroupMembership membership : selfMemberships) {
            Group group = getGroup(membership.getGroupId());
            groups.add(group);
        }
        return groups;
    }
}
