package com.along.coash.server.admin.contract.post;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Post extends PostBase {

    private String id;

    private LocalDateTime createTime;

}
