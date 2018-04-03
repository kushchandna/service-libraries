package com.kush.usergroups;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;
import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public class DefaultUserGroupPersistor extends DelegatingPersistor<UserGroup> implements UserGroupPersistor {

    private final Persistor<UserGroupMember> memberPersistor;

    public DefaultUserGroupPersistor(Persistor<UserGroup> delegate, Persistor<UserGroupMember> memberPersistor) {
        super(delegate);
        this.memberPersistor = memberPersistor;
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

    @Override
    public UserGroupMember addGroupMember(Identifier groupId, Identifier userId, LocalDateTime memberSince, Identifier addedBy)
            throws PersistorOperationFailedException {
        UserGroupMember groupMember = new UserGroupMember(groupId, userId, memberSince, addedBy);
        return memberPersistor.save(groupMember);
    }

    @Override
    public List<UserGroupMember> getGroupMembers(Identifier groupId) throws PersistorOperationFailedException {
        Iterator<UserGroupMember> allGroupMembers = memberPersistor.fetchAll();
        return Streams.stream(allGroupMembers).filter(m -> m.getGroupId().equals(groupId)).collect(Collectors.toList());
    }
}
