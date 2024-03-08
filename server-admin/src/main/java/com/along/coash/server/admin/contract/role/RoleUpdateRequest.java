package com.along.coash.server.admin.contract.role;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class RoleUpdateRequest extends RoleBase {

    @NotNull(message = "角色编号不能为空")
    private String id;

    private Integer status;
}
