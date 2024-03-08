package com.along.coash.server.admin.contract.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class UserUpdateRequest extends UserBase {

    @NotNull(message = "用户编号不能为空")
    private String id;

    @NotEmpty(message = "密码不能为空")
    @Size(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String password;

    @NotNull(message = "状态不能为空")
    private Integer status;
}
