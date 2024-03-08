package com.along.coash.server.admin.contract.dict.dictData;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class DictDataUpdateRequest extends DictDataBase {

    @NotNull(message = "字典数据编号不能为空")
    private Long id;

}
