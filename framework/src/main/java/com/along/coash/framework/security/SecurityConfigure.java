package com.along.coash.framework.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * HTTP请求安全配置接口
 */
public interface SecurityConfigure {
    void configHttpSecurity(HttpSecurity httpSecurity) throws Exception;
}
