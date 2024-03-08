package com.along.coash.server.admin.contract.role;

import com.along.coash.framework.contract.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
public class RoleQueryRequest extends PageParam {

    private String name;

    private String code;

    private Integer status;

    private LocalDateTime startCreateTime;

    private LocalDateTime endCreateTime;

}
