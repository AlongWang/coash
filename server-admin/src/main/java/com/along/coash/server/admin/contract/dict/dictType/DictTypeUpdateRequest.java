package com.along.coash.server.admin.contract.dict.dictType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class DictTypeUpdateRequest extends DictTypeBase {

    @NotNull(message = "字典类型编号不能为空")
    private String id;

}
