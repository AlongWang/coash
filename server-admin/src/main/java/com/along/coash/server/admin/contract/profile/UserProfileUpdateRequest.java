package com.along.coash.server.admin.contract.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UserProfileUpdateRequest {

    @Size(max = 30, message = "用户昵称长度不能超过 30 个字符")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过 50 个字符")
    private String email;

    @Size(min = 11, max = 11, message = "手机号长度必须 11 位")
    private String mobile;

    private Integer sex;

}
