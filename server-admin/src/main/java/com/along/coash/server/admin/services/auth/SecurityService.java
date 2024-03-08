package com.along.coash.server.admin.services.auth;

import com.along.coash.server.admin.services.permission.PermissionService;
import com.along.coash.server.admin.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ss")
public class SecurityService {

    @Autowired
    private PermissionService permissionService;
    public boolean hasPermission(String permission) {
        return hasAnyPermissions(permission);
    }

    public boolean hasAnyPermissions(String... permissions) {
        return permissionService.hasAnyPermissions(AuthUtils.getLoginUserId(), permissions);
    }
    public boolean hasRole(String role) {
        return hasAnyRoles(role);
    }
    public boolean hasAnyRoles(String... roles) {
        return permissionService.hasAnyRoles(AuthUtils.getLoginUserId(), roles);
    }
}
