package com.along.coash.framework.auth;

import com.along.coash.framework.security.ISecurityFilter;
import com.along.coash.framework.security.SecurityConfigure;
import com.along.coash.framework.security.SecurityConfigureComposite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.CollectionUtils;


import java.util.List;

@Configuration
@ConditionalOnMissingBean(AuthenticationConfigure.class)
@EnableMethodSecurity(securedEnabled = true)
public class AuthenticationConfigure {
    protected final SecurityConfigureComposite composite = new SecurityConfigureComposite();

    @Autowired(required = false)
    public void setConfigurers(List<SecurityConfigure> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            composite.addSecurityConfigure(configurers);
        }
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new UnAuthorizedAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new ForbiddenAccessDeniedHandler();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, ISecurityFilter authenticationTokenFilter,
                                            AuthenticationEntryPoint authenticationEntryPoint,
                                            AccessDeniedHandler accessDeniedHandler) throws Exception {
        httpSecurity.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .exceptionHandling((eh -> eh.authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)));

        configHttpSecurity(httpSecurity);

        httpSecurity.authorizeHttpRequests(registry -> registry.anyRequest().authenticated());
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    protected void configHttpSecurity(HttpSecurity httpSecurity) throws Exception {
        composite.configHttpSecurity(httpSecurity);
    }

}
