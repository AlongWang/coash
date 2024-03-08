package com.along.coash.server.admin.contract.dict.dictData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DictDataResponse extends DictDataBase {

    private Long id;

    private LocalDateTime createTime;

}
