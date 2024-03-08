package com.along.coash.server.admin.contract.notice;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeUpdateRequest extends NoticeBase {

    @NotNull(message = "岗位公告编号不能为空")
    private String id;

}
