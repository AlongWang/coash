package com.along.coash.server.admin.config.security;

import com.along.coash.framework.security.SecurityConfigure;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class ServerSecurityConfigure implements SecurityConfigure {
    @Override
    public void configHttpSecurity(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(registry -> registry.requestMatchers("/admin-api/system/auth/login").permitAll())//登陆接口
                .authorizeHttpRequests(registry -> registry.requestMatchers("/admin-api/system/auth/refresh-token").permitAll())//token刷新接口
                .authorizeHttpRequests(registry -> registry.requestMatchers("/admin-api/system/dict-data/list-all-simple").permitAll());//字典列表接口
    }
}
