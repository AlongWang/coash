package com.along.coash.server.admin.config.auth;

import com.along.coash.framework.auth.AuthenticationConfigure;
import com.along.coash.framework.exception.GlobalExceptionHandler;
import com.along.coash.server.admin.services.token.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 授权自动配置文件
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class AuthenticationAutoConfig extends AuthenticationConfigure {

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilter(TokenService tokenService,
                                                               GlobalExceptionHandler globalExceptionHandler) {
        return new AuthenticationTokenFilter(tokenService, globalExceptionHandler);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
}
