package com.along.coash.server.admin.contract.notify;

import com.along.coash.framework.contract.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MyNotifyMessageQueryRequest extends PageParam {


    private Boolean readStatus;


    private LocalDateTime startCreateTime;

    private LocalDateTime endCreateTime;

}
