package com.along.coash.server.admin.contract.dict.dictType;


import com.along.coash.framework.contract.PageParam;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
public class DictTypeQueryRequest extends PageParam {

    private String name;

    @Size(max = 100, message = "字典类型类型长度不能超过100个字符")
    private String type;

    private Integer status;

    private LocalDateTime startCreateTime;

    private LocalDateTime endCreateTime;

}
