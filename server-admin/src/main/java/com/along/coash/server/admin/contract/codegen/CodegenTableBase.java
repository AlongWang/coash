package com.along.coash.server.admin.contract.codegen;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CodegenTableBase {

    @NotNull(message = "导入类型不能为空")
    private Integer scene;

    @NotNull(message = "表名称不能为空")
    private String tableName;

    @NotNull(message = "表描述不能为空")
    private String tableComment;

    private String remark;

    @NotNull(message = "模块名不能为空")
    private String moduleName;

    @NotNull(message = "业务名不能为空")
    private String businessName;

    @NotNull(message = "类名称不能为空")
    private String className;

    @NotNull(message = "类描述不能为空")
    private String classComment;

    @NotNull(message = "作者不能为空")
    private String author;

    @NotNull(message = "模板类型不能为空")
    private Integer templateType;

    @NotNull(message = "前端类型不能为空")
    private Integer frontType;

    private Long parentMenuId;

}
