package com.along.coash.server.admin.contract.log;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 登录日志 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class LoginLogBase {

    @NotNull(message = "日志类型不能为空")
    private Integer logType;

    @NotEmpty(message = "链路追踪编号不能为空")
    private String traceId;

    @NotBlank(message = "用户账号不能为空")
    @Size(max = 30, message = "用户账号长度不能超过30个字符")
    private String username;

    @NotNull(message = "登录结果不能为空")
    private Integer result;

    @NotEmpty(message = "用户 IP 不能为空")
    private String userIp;

    private String userAgent;

}
