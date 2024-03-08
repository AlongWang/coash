package com.along.coash.server.admin.contract.log;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LoginLogResponse extends LoginLogBase {

    private String id;

    private String userId;

    @NotNull(message = "用户类型不能为空")
    private Integer userType;

    private LocalDateTime createTime;

}
