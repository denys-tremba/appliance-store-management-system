package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtConfigurer extends AbstractHttpConfigurer<JwtConfigurer, HttpSecurity> {

    @Override
    public void init(HttpSecurity builder) throws Exception {
        super.init(builder);
    }


    @Override
    public void configure(HttpSecurity builder) throws Exception {
        super.configure(builder);
        ApplicationContext context = builder.getSharedObject(ApplicationContext.class);
        builder.addFilterBefore(context.getBean(JwtAccessFilter.class), UsernamePasswordAuthenticationFilter.class);
        builder.addFilterBefore(context.getBean(JwtRefreshFilter.class), JwtAccessFilter.class);
    }
}
