package com.along.coash.server.admin.contract.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Role extends RoleBase {

    private String id;

    private Integer dataScope;

    private Set<String> dataScopeDeptIds;

    private Integer status;

    private Integer type;

    private LocalDateTime createTime;

}
