package com.along.coash.server.admin.contract.menu;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;



@Data
@EqualsAndHashCode(callSuper = true)
public class MenuUpdateRequest extends MenuBase {

    
    @NotNull(message = "菜单编号不能为空")
    private String id;

}
