package com.along.coash.server.admin.contract.role;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class RoleUpdateStatusRequest {

    @NotNull(message = "角色编号不能为空")
    private String id;

    @NotNull(message = "状态不能为空")
    private Integer status;

}
