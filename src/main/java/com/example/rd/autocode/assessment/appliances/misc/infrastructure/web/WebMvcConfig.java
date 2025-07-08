package com.example.rd.autocode.assessment.appliances.misc.infrastructure.web;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.security.JwtCookieOrderHandlerInterceptor;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Configuration
@ComponentScan
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    ApplianceLastUpdatedPrinter applianceLastUpdatedPrinter;
    @Autowired
    JwtCookieOrderHandlerInterceptor jwtCookieOrderHandlerInterceptor;

    @Bean
    public LocaleResolver localeResolver() {
        return new CookieLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeInterceptor() {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang");
        return localeInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeInterceptor());
        registry.addInterceptor(jwtCookieOrderHandlerInterceptor).addPathPatterns("/orders/current/**");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addPrinter(applianceLastUpdatedPrinter);
    }


    @Bean
    @RequestScope
    public ServletUriComponentsBuilder uriComponentBuilder() {
        return ServletUriComponentsBuilder.fromCurrentRequest();
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}
