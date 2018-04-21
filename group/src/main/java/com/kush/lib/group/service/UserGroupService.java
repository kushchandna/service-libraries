package com.kush.lib.group.service;

import java.time.Clock;
import java.time.LocalDateTime;

import com.kush.lib.group.entities.Group;
import com.kush.lib.group.persistors.GroupPersistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.server.BaseService;
import com.kush.utils.id.Identifier;

public class UserGroupService extends BaseService {

    public Group createGroup(String groupName) throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        LocalDateTime createdAt = getCurrentDateTime();
        GroupPersistor groupPersistor = getInstance(GroupPersistor.class);
        return groupPersistor.createGroup(groupName, currentUserId, createdAt);
    }

    private LocalDateTime getCurrentDateTime() {
        Clock clock = getInstance(Clock.class);
        return LocalDateTime.now(clock);
    }
}
