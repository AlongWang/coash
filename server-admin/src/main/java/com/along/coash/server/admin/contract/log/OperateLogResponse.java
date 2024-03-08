package com.along.coash.server.admin.contract.log;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OperateLogResponse extends OperateLogBase {

    private String id;

    private String userNickname;

}
