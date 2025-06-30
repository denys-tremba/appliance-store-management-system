package com.example.rd.autocode.assessment.appliances.user.login;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = LoginController.class)
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


    @Test
    void resetPassword() {
        mvcTester.post().with(csrf()).uri("/login/ott").exchange().assertThat().hasStatus3xxRedirection();
    }
}