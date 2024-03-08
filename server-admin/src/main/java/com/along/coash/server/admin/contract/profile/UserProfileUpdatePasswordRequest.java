package com.along.coash.server.admin.contract.profile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileUpdatePasswordRequest {

    @NotEmpty(message = "旧密码不能为空")
    @Size(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String oldPassword;

    @NotEmpty(message = "新密码不能为空")
    @Size(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String newPassword;

}
