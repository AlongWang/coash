package com.along.coash.server.admin.contract.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
public class User extends UserBase {

    private String id;

    private Integer status;

    private String loginIp;

    private LocalDateTime loginDate;

    private LocalDateTime createTime;

}
