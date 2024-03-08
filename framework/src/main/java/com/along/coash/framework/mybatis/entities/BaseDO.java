package com.along.coash.framework.mybatis.entities;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体对象
 *
 * @author 芋道源码
 */
@Data
@Accessors(chain = true)
public abstract class BaseDO implements Serializable {

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建者，目前使用 SysUser 的 id 编号
     *
     */
    private String creator;
    /**
     * 更新者，目前使用 SysUser 的 id 编号
     *
     */
    private String updater;
    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;

}
