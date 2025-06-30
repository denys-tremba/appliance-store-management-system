package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.DispatcherTypeRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
@ComponentScan(basePackages = "com.example.rd.autocode.assessment.appliances.misc.infrastructure.security")
public class SecurityConfig {
    @Autowired
    OneTimeTokenGenerationSuccessHandler oneTimeTokenGenerationSuccessHandler;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, LoginLimitingAuthProviderProxy providerProxy) throws Exception {
        return http
                .authorizeHttpRequests(c->c
                            .requestMatchers(new DispatcherTypeRequestMatcher(DispatcherType.ERROR)).permitAll()
                            .requestMatchers(HttpMethod.GET, "/login/ott/username").permitAll()
                            .requestMatchers("/clients/signUp", "/employees/signUp").permitAll()
                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                            .requestMatchers(PathRequest.toH2Console()).permitAll()
                            .anyRequest().authenticated()
                        )
                .headers((headers) -> headers.frameOptions((frame) -> frame.sameOrigin()))
                .csrf(c->c.ignoringRequestMatchers(PathRequest.toH2Console()))
                .formLogin(customizer-> customizer
                        .loginPage("/login").permitAll()
                )
                .oneTimeTokenLogin(c->c.tokenGenerationSuccessHandler(oneTimeTokenGenerationSuccessHandler)
                        .loginPage("/login"))
                .oauth2Client(Customizer.withDefaults())
                // CSRF protection is triggered for POST /logout but GET
                .logout(c->c.logoutRequestMatcher(new OrRequestMatcher(
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET,"/logout"),
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST,"/logout")
                        )
                ))
                .authenticationProvider(providerProxy)
                .authenticationProvider(new AdminAuthenticationProvider())
                .build();
    }



    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("CLIENT", "EMPLOYEE")
                .build();
    }
}

//                .objectPostProcessor(new ObjectPostProcessor<Object>() {
//@Override
//public <O extends Object> O postProcess(O object) {
//        return object;
//        }
//        })