package com.along.coash.server.admin.contract.profile;

import com.along.coash.server.admin.contract.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class UserProfile extends User {

    /**
     * 所属角色
     */
    private List<Role> roles;

    /**
     * 所在部门
     */
    private Dept dept;

    /**
     * 所属岗位数组
     */
    private List<Post> posts;

    @Data
    public static class Role {

        private String id;

        private String name;

    }

    @Data
    public static class Dept {

        private String id;

        private String name;

    }

    @Data
    public static class Post {

        private String id;

        private String name;

    }

}
