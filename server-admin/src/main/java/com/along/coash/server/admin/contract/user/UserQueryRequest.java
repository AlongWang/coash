package com.along.coash.server.admin.contract.user;

import com.along.coash.framework.contract.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserQueryRequest extends PageParam {

    private String username;

    private String mobile;

    private Integer status;

    private LocalDateTime startCreateTime;

    private LocalDateTime endCreateTime;

    private String deptId;

}
