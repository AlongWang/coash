package com.along.coash.server.admin.contract.dept;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeptInfo extends DeptBaseInfo {

    private String id;

    private Integer status;

    private LocalDateTime createTime;

}
