package com.along.coash.server.admin.contract.permission;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Collections;
import java.util.Set;

@Data
public class PermissionAssignRoleMenuRequest {

    @NotNull(message = "角色编号不能为空")
    private String roleId;

    private Set<String> menuIds = Collections.emptySet(); // 兜底

}
