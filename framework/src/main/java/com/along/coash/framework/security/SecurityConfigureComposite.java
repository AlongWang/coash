package com.along.coash.framework.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class SecurityConfigureComposite implements SecurityConfigure {
    private final List<SecurityConfigure> delegates = new ArrayList<>();

    public void addSecurityConfigure(List<SecurityConfigure> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.delegates.addAll(configurers);
        }
    }

    @Override
    public final void configHttpSecurity(HttpSecurity httpSecurity) throws Exception {
        for (SecurityConfigure delegate : delegates) {
            delegate.configHttpSecurity(httpSecurity);
        }
    }
}
