package com.along.coash.server.admin.contract.auth;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class PermissionInfo {

    private UserVO user;

    private Set<String> roles;

    private Set<String> permissions;

    private List<MenuVO> menus;

    @Data
    @Builder
    public static class UserVO {

        private String id;

        private String nickname;

        private String avatar;

    }

    @Data
    @Builder
    public static class MenuVO {

        private String id;

        private String parentId;

        private String name;

        private String path;

        private String component;

        private String componentName;

        private String icon;

        private Boolean visible;

        private Boolean keepAlive;

        private Boolean alwaysShow;

        /**
         * 子路由
         */
        private List<MenuVO> children;

    }

}
