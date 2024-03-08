package com.along.coash.server.admin.contract.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserPageItem extends User {

    /**
     * 所在部门
     */
    private Dept dept;

    @Data
    public static class Dept {

        private String id;

        private String name;

    }

}
