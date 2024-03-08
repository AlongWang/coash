package com.along.coash.server.admin.contract.codegen;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CodegenTableResponse extends CodegenTableBase {

    private String id;

    private Integer dataSourceConfigId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
