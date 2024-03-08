package com.along.coash.server.admin.contract.post;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class PostUpdateRequest extends PostBase {

    @NotNull(message = "岗位编号不能为空")
    private String id;

}
