package com.kush.usergroups;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.server.BaseService;
import com.kush.utils.id.Identifier;

public class UserGroupService extends BaseService {

    public UserGroup createGroup(String groupName) throws ServiceRequestFailedException {
        Identifier currentUser = getCurrentUser().getId();
        UserGroupPersistor persistor = getInstance(UserGroupPersistor.class);
        LocalDateTime creationDate = LocalDateTime.now(getClock());
        try {
            UserGroup createdGroup = persistor.createGroup(groupName, currentUser, creationDate);
            persistor.addGroupMember(createdGroup.getId(), currentUser, creationDate, currentUser);
            return createdGroup;
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    public UserGroup getGroup(Identifier groupId) throws ServiceRequestFailedException {
        UserGroupPersistor persistor = getInstance(UserGroupPersistor.class);
        try {
            return persistor.getGroup(groupId);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    public List<UserGroupMember> getGroupMembers(Identifier groupId) throws ServiceRequestFailedException {
        UserGroupPersistor persistor = getInstance(UserGroupPersistor.class);
        try {
            return persistor.getGroupMembers(groupId);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    public UserGroupMember addMemberToGroup(Identifier groupId, Identifier userId) throws ServiceRequestFailedException {
        Identifier currentUser = getCurrentUser().getId();
        LocalDateTime memberSince = LocalDateTime.now(getClock());
        UserGroupPersistor persistor = getInstance(UserGroupPersistor.class);
        try {
            return persistor.addGroupMember(groupId, userId, memberSince, currentUser);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    private Clock getClock() {
        return getInstance(Clock.class);
    }
}
