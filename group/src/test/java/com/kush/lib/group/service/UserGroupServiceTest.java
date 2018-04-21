package com.kush.lib.group.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.kush.lib.group.entities.DefaultGroupPersistor;
import com.kush.lib.group.entities.Group;
import com.kush.lib.group.persistors.GroupPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseServiceTest;

public class UserGroupServiceTest extends BaseServiceTest {

    private UserGroupService userGroupService;

    @Before
    public void beforeEachTest() throws Exception {
        userGroupService = new UserGroupService();
        registerService(userGroupService);

        Persistor<Group> delegateGroupPersistor = InMemoryPersistor.forType(Group.class);
        addToContext(GroupPersistor.class, new DefaultGroupPersistor(delegateGroupPersistor));
    }

    @Test
    public void createEmptyUserGroup() throws Exception {
        String testGroupName = "Test Group";

        User user = getUser(0);
        runAuthenticatedOperation(() -> {
            Group group = userGroupService.createGroup(testGroupName);
            assertThat(group.getId(), is(notNullValue()));
            assertThat(group.getGroupName(), is(equalTo(testGroupName)));
            assertThat(group.getOwner(), is(equalTo(user.getId())));
            assertThat(group.getCreatedAt(), is(equalTo(getCurrentDateTime())));
        });
    }
}
