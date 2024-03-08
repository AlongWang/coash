package com.along.coash.server.admin.contract.codegen;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CodegenColumnBase {

    @NotNull(message = "表编号不能为空")
    private Long tableId;

    @NotNull(message = "字段名不能为空")
    private String columnName;

    @NotNull(message = "字段类型不能为空")
    private String dataType;

    @NotNull(message = "字段描述不能为空")
    private String columnComment;

    @NotNull(message = "是否允许为空不能为空")
    private Boolean nullable;

    @NotNull(message = "是否主键不能为空")
    private Boolean primaryKey;

    @NotNull(message = "是否自增不能为空")
    private String autoIncrement;

    @NotNull(message = "排序不能为空")
    private Integer ordinalPosition;

    @NotNull(message = "Java 属性类型不能为空")
    private String javaType;

    @NotNull(message = "Java 属性名不能为空")
    private String javaField;

    private String dictType;

    private String example;

    @NotNull(message = "是否为 Create 创建操作的字段不能为空")
    private Boolean createOperation;

    @NotNull(message = "是否为 Update 更新操作的字段不能为空")
    private Boolean updateOperation;

    @NotNull(message = "是否为 List 查询操作的字段不能为空")
    private Boolean listOperation;

    @NotNull(message = "List 查询操作的条件类型不能为空")
    private String listOperationCondition;

    @NotNull(message = "是否为 List 查询操作的返回字段不能为空")
    private Boolean listOperationResult;

    @NotNull(message = "显示类型不能为空")
    private String htmlType;

}
