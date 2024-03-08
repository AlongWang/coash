package com.along.coash.server.admin.contract.codegen;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CodegenCreateListRequest {

    @NotNull(message = "数据源配置的编号不能为空")
    private String dataSourceConfigId;

    @NotNull(message = "表名数组不能为空")
    private List<String> tableNames;

}
