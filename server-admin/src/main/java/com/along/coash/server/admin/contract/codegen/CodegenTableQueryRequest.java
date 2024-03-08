package com.along.coash.server.admin.contract.codegen;

import com.along.coash.framework.contract.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CodegenTableQueryRequest extends PageParam {

    private String tableName;

    private String tableComment;

    private String className;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
