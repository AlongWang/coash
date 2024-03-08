package com.along.coash.server.admin.controller;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.framework.contract.CommonStatusEnum;
import com.along.coash.server.admin.contract.role.*;
import com.along.coash.server.admin.entities.RoleDO;
import com.along.coash.server.admin.services.permission.RoleService;
import com.along.coash.server.admin.converter.RoleConverter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

import static java.util.Collections.singleton;

@RestController
@RequestMapping("/admin-api/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('system:role:create')")
    public CommonResult<String> createRole(@Valid @RequestBody RoleCreateRequest request) {
        return CommonResult.success(String.valueOf(roleService.createRole(request, null)));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('system:role:update')")
    public CommonResult<Boolean> updateRole(@Valid @RequestBody RoleUpdateRequest request) {
        roleService.updateRole(request);
        return CommonResult.success(true);
    }

    @PutMapping("/update-status")
    @PreAuthorize("@ss.hasPermission('system:role:update')")
    public CommonResult<Boolean> updateRoleStatus(@Valid @RequestBody RoleUpdateStatusRequest request) {
        roleService.updateRoleStatus(Long.parseLong(request.getId()), request.getStatus());
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('system:role:delete')")
    public CommonResult<Boolean> deleteRole(@RequestParam("id") String id) {
        roleService.deleteRole(Long.parseLong(id));
        return CommonResult.success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('system:role:query')")
    public CommonResult<Role> getRole(@RequestParam("id") String id) {
        RoleDO role = roleService.getRole(Long.parseLong(id));
        return CommonResult.success(RoleConverter.INSTANCE.convert(role));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('system:role:query')")
    public CommonResult<IPage<Role>> getRolePage(RoleQueryRequest reqVO) {
        return CommonResult.success(RoleConverter.INSTANCE.convert(roleService.getRolePage(reqVO)));
    }

    @GetMapping("/list-all-simple")
    public CommonResult<List<RoleSimple>> getSimpleRoleList() {
        // 获得角色列表，只要开启状态的
        List<RoleDO> list = roleService.getRoleListByStatus(singleton(CommonStatusEnum.ENABLE.getStatus()));
        // 排序后，返回给前端
        list.sort(Comparator.comparing(RoleDO::getSort));
        return CommonResult.success(RoleConverter.INSTANCE.convert(list));
    }

}
