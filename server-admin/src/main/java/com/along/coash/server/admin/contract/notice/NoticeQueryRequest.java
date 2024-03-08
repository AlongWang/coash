package com.along.coash.server.admin.contract.notice;

import com.along.coash.framework.contract.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeQueryRequest extends PageParam {

    private String title;

    private Integer status;

}
