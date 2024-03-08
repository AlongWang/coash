package com.along.coash.server.admin.contract.codegen.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 代码生成的场景枚举
 *
 * @author 芋道源码
 */
@AllArgsConstructor
@Getter
public enum CodegenSceneEnum {

    ADMIN(1, "管理后台", "admin", ""),
    APP(2, "用户 APP", "app", "App");

    /**
     * 场景
     */
    private final Integer scene;
    /**
     * 场景名
     */
    private final String name;
    /**
     * 基础包名
     */
    private final String basePackage;
    /**
     * Controller 和 VO 类的前缀
     */
    private final String prefixClass;

    public static CodegenSceneEnum valueOf(Integer scene) {
        for (CodegenSceneEnum value : CodegenSceneEnum.values()) {
            if (value.getScene().equals(scene)) {
                return value;
            }
        }
        return null;
    }

}
