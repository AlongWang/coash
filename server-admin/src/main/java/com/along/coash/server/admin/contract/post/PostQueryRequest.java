package com.along.coash.server.admin.contract.post;

import com.along.coash.framework.contract.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PostQueryRequest extends PageParam {

    private String code;

    private String name;

    private Integer status;

}
