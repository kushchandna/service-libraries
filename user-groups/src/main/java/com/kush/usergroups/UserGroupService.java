package com.kush.usergroups;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.server.BaseService;
import com.kush.utils.id.Identifier;

public class UserGroupService extends BaseService {

    public UserGroup createGroup(String groupName) throws ServiceRequestFailedException {
        Identifier currentUser = getCurrentUser().getId();
        Clock clock = getInstance(Clock.class);
        UserGroupPersistor persistor = getInstance(UserGroupPersistor.class);
        try {
            return persistor.createGroup(groupName, currentUser, LocalDateTime.now(clock));
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

    public List<UserGroupMember> getGroupMembers(Identifier id) {
        return Collections.emptyList();
    }
}
