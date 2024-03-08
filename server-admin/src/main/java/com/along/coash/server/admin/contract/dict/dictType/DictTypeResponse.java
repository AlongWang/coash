package com.along.coash.server.admin.contract.dict.dictType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DictTypeResponse extends DictTypeBase {

    private Long id;

    private String type;

    private LocalDateTime createTime;

}
