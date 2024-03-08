package com.along.coash.server.admin.contract.menu;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Menu extends MenuBase {

    private String id;

    
    private Integer status;

    
    private LocalDateTime createTime;

}
