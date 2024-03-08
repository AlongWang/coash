package com.along.coash.server.admin.contract.dict.dictData;

import com.along.coash.framework.contract.PageParam;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class DictDataQueryRequest extends PageParam {

    @Size(max = 100, message = "字典标签长度不能超过100个字符")
    private String label;

    @Size(max = 100, message = "字典类型类型长度不能超过100个字符")
    private String dictType;

    private Integer status;

}
