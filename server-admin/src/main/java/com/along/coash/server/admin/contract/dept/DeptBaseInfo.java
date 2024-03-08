package com.along.coash.server.admin.contract.dept;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeptBaseInfo {

    @NotBlank(message = "部门名称不能为空")
    @Size(max = 30, message = "部门名称长度不能超过30个字符")
    private String name;

    private String parentId;

    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    private String leaderUserId;

    @Size(max = 11, message = "联系电话长度不能超过11个字符")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    @NotNull(message = "状态不能为空")
    private Integer status;

}
