package com.example.rd.autocode.assessment.appliances.misc.infrastructure.notification.smtp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;

@Configuration(proxyBeanMethods = false)
public class EmailTemplateResolverConfig {
    @Bean
    public SpringResourceTemplateResolver emailBodyThymeleafTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:");
        resolver.setSuffix(".st");
        resolver.setTemplateMode(TemplateMode.TEXT);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCacheable(true);
        resolver.getResolvablePatternSpec().addPattern("email/*");
        resolver.setOrder(Ordered.LOWEST_PRECEDENCE);
        return resolver;
    }
}
