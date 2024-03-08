package com.along.coash.server.admin.contract.log;

import com.along.coash.framework.contract.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Data
public class OperateLogQueryRequest extends PageParam {

    private String module;

    private String userNickname;

    private Integer type;

    private Boolean success;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
