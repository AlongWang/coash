package com.along.coash.server.admin.contract.dept;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class DeptUpdateRequest extends DeptBaseInfo {

    @NotNull(message = "部门编号不能为空")
    private String id;

}
