package com.along.coash.server.admin.contract.notice;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeResponse extends NoticeBase {

    private String id;

    private LocalDateTime createTime;

}
