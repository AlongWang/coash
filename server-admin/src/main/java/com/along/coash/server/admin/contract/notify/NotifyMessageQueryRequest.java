package com.along.coash.server.admin.contract.notify;

import com.along.coash.framework.contract.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NotifyMessageQueryRequest extends PageParam {

    private Long userId;


    private Integer userType;


    private String templateCode;


    private Integer templateType;


    private LocalDateTime startCreateTime;

    private LocalDateTime endCreateTime;

}
