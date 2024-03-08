package com.along.coash.server.admin.contract.permission;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Collections;
import java.util.Set;

@Data
public class PermissionAssignUserRoleRequest {

    @NotNull(message = "用户编号不能为空")
    private String userId;

    private Set<String> roleIds = Collections.emptySet(); // 兜底

}
