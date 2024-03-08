package com.along.coash.server.admin.contract.codegen;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CodegenColumnResponse extends CodegenColumnBase {

    private String id;

    private LocalDateTime createTime;

}
