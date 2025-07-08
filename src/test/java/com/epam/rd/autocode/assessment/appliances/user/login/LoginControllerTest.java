package com.example.rd.autocode.assessment.appliances.user.login;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(controllers = LoginController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.example\\.rd\\.autocode\\.assessment\\.appliances\\.misc\\.infrastructure\\..*")})

class LoginControllerTest {
    @TestConfiguration
    static class TestConfig {
        @Bean
        SecurityFilterChain testFilterChain(HttpSecurity sec) throws Exception {
            return sec.authorizeHttpRequests(c -> c.requestMatchers("/login/**").permitAll().anyRequest().authenticated()).build();
        }
    }

    @Autowired
    MockMvcTester mvcTester;

    @Test
    void login() {
        mvcTester.get().uri("/login").exchange().assertThat().hasStatusOk();
    }

    @Test
    void getUsernameForm() {
        mvcTester.get().uri("/login/ott/username").exchange().assertThat().hasStatusOk();
    }
}