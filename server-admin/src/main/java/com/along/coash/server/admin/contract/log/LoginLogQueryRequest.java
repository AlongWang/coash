package com.along.coash.server.admin.contract.log;

import com.along.coash.framework.contract.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
public class LoginLogQueryRequest extends PageParam {

    private String userIp;

    private String username;

    private Boolean status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
