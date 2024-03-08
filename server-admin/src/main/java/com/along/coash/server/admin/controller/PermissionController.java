package com.along.coash.server.admin.controller;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.server.admin.contract.permission.PermissionAssignRoleDataScopeRequest;
import com.along.coash.server.admin.contract.permission.PermissionAssignRoleMenuRequest;
import com.along.coash.server.admin.contract.permission.PermissionAssignUserRoleRequest;
import com.along.coash.server.admin.services.permission.PermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin-api/system/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/list-role-menus")
    @PreAuthorize("@ss.hasPermission('system:permission:assign-role-menu')")
    public CommonResult<Set<String>> getRoleMenuList(String roleId) {
        return CommonResult.success(permissionService.getRoleMenuListByRoleId(Collections.singleton(Long.valueOf(roleId)))
                .stream().map(String::valueOf).collect(Collectors.toSet()));
    }

    @PostMapping("/assign-role-menu")
    @PreAuthorize("@ss.hasPermission('system:permission:assign-role-menu')")
    public CommonResult<Boolean> assignRoleMenu(@Validated @RequestBody PermissionAssignRoleMenuRequest request) {
        // 执行菜单的分配
        permissionService.assignRoleMenu(Long.parseLong(request.getRoleId()),
                request.getMenuIds().stream().map(Long::valueOf).collect(Collectors.toSet()));
        return CommonResult.success(true);
    }

    @PostMapping("/assign-role-data-scope")
    @PreAuthorize("@ss.hasPermission('system:permission:assign-role-data-scope')")
    public CommonResult<Boolean> assignRoleDataScope(@Valid @RequestBody PermissionAssignRoleDataScopeRequest request) {
        permissionService.assignRoleDataScope(Long.parseLong(request.getRoleId()), request.getDataScope(),
                request.getDataScopeDeptIds());
        return CommonResult.success(true);
    }

    @GetMapping("/list-user-roles")
    @PreAuthorize("@ss.hasPermission('system:permission:assign-user-role')")
    public CommonResult<Set<String>> listAdminRoles(@RequestParam("userId") String userId) {
        return CommonResult.success(permissionService.getUserRoleIdListByUserId(Long.valueOf(userId))
                .stream().map(String::valueOf).collect(Collectors.toSet()));
    }

    @PostMapping("/assign-user-role")
    @PreAuthorize("@ss.hasPermission('system:permission:assign-user-role')")
    public CommonResult<Boolean> assignUserRole(@Validated @RequestBody PermissionAssignUserRoleRequest request) {
        permissionService.assignUserRole(Long.parseLong(request.getUserId()),
                request.getRoleIds().stream().map(Long::valueOf).collect(Collectors.toSet()));
        return CommonResult.success(true);
    }

}
