package com.kush.lib.auth.authorization;

import org.junit.Test;

public class SamplePermissionE2E {

    @Test
    public void permissionToSameNode() throws Exception {
        Permission perm1 = Permissions.create("applications", "sample-application", "modifications", "sample-module1");
        Permission perm2 = Permissions.create("applications", "sample-application", "querying", "sample-module2");
        perm1.toString();
        perm2.toString();
    }
}
