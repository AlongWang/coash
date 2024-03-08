package com.along.coash.server.admin.contract.permission;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Collections;
import java.util.Set;

@Data
public class PermissionAssignRoleDataScopeRequest {

    @NotNull(message = "角色编号不能为空")
    private String roleId;

    @NotNull(message = "数据范围不能为空")
    private Integer dataScope;

    private Set<Long> dataScopeDeptIds = Collections.emptySet(); // 兜底

}
