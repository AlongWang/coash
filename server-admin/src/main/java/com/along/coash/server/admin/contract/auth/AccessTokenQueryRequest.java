package com.along.coash.server.admin.contract.auth;

import com.along.coash.framework.contract.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccessTokenQueryRequest extends PageParam {

    private String userId;

    private Integer userType;

    private String clientId;

}
