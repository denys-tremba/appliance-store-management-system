package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.savedrequest.CookieRequestCache;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.DispatcherTypeRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
@ComponentScan(basePackages = "com.example.rd.autocode.assessment.appliances.misc.infrastructure.security")
public class SecurityConfig {
    @Bean
    @Profile("default")
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, LoginLimitingAuthProviderProxy providerProxy, CookieRequestCache requestCache, JwtCookieEnricherSuccessHandlerDecorator successHandler, OneTimeTokenGenerationSuccessHandler oneTimeTokenGenerationSuccessHandler) throws Exception {
        return http
                .authorizeHttpRequests(c->c
                            .requestMatchers("/login").permitAll()
                            .requestMatchers(new DispatcherTypeRequestMatcher(DispatcherType.ERROR)).permitAll()
                            .requestMatchers(HttpMethod.GET, "/login/ott/username").permitAll()
                            .requestMatchers("/clients/signUp", "/employees/signUp", "/appliances", "/manufacturers", "/").permitAll()
                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                            .requestMatchers(PathRequest.toH2Console()).permitAll()
                                .anyRequest().authenticated()
                        )
                .headers((headers) -> headers.frameOptions((frame) -> frame.sameOrigin()))
                .csrf(c->c.disable())
                .logout(c->c.deleteCookies(JwtCookieFactory.JWT_ACCESS, JwtCookieFactory.JWT_ORDER, JwtCookieFactory.JWT_REFRESH)
                            .logoutRequestMatcher(logoutRequestMatcher()))
                .formLogin(c->c.loginPage("/login")
                               .successHandler(successHandler)
                               .loginProcessingUrl("/login"))
                .sessionManagement(c->c
                        .sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy())
                        .sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .authenticationProvider(providerProxy)
//                .authenticationProvider(new AdminAuthenticationProvider())
                .exceptionHandling(c->c.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
                .requestCache(c->c.requestCache(requestCache))
                .with(new JwtConfigurer(), Customizer.withDefaults())
                .build();
    }

    @Bean
    @Profile("prod")
    SecurityFilterChain prodSecurityFilterChain(HttpSecurity http, LoginLimitingAuthProviderProxy providerProxy, CookieRequestCache requestCache, JwtCookieEnricherSuccessHandlerDecorator successHandler, OneTimeTokenGenerationSuccessHandler oneTimeTokenGenerationSuccessHandler) throws Exception {
        return http
                .authorizeHttpRequests(c->c
                        .requestMatchers("/login").permitAll()
                        .requestMatchers(new DispatcherTypeRequestMatcher(DispatcherType.ERROR)).permitAll()
                        .requestMatchers(HttpMethod.GET, "/login/ott/username").permitAll()
                        .requestMatchers("/clients/signUp", "/employees/signUp", "/appliances", "/manufacturers", "/").permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated()
                )
                .headers((headers) -> headers.frameOptions((frame) -> frame.sameOrigin()))
                .csrf(c->c.disable())
                .logout(c->c.deleteCookies(JwtCookieFactory.JWT_ACCESS, JwtCookieFactory.JWT_ORDER, JwtCookieFactory.JWT_REFRESH)
                        .logoutRequestMatcher(logoutRequestMatcher()))
                .formLogin(c->c.loginPage("/login")
                        .successHandler(successHandler)
                        .loginProcessingUrl("/login"))
                .sessionManagement(c->c
                        .sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy())
                        .sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .authenticationProvider(providerProxy)
                .exceptionHandling(c->c.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
                .requestCache(c->c.requestCache(requestCache))
                .with(new JwtConfigurer(), Customizer.withDefaults())
                .build();
    }

    private RequestMatcher logoutRequestMatcher() {
        return new OrRequestMatcher(
                PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/logout"),
                PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/logout")
        );
    }

    @Bean
    CookieRequestCache cookieRequestCache() {
        CookieRequestCache requestCache = new CookieRequestCache();
        return requestCache;
    }

    @Bean
    JwtCookieEnricherSuccessHandlerDecorator jwtCookieEnricherSuccessHandlerDecorator(CookieRequestCache cookieRequestCache, JwtService jwtService, JwtCookieFactory jwtCookieFactory) {
        SavedRequestAwareAuthenticationSuccessHandler delegate = new SavedRequestAwareAuthenticationSuccessHandler();
        delegate.setUseReferer(true);
        delegate.setRequestCache(cookieRequestCache);
        JwtCookieEnricherSuccessHandlerDecorator successHandler = new JwtCookieEnricherSuccessHandlerDecorator(delegate, jwtService, jwtCookieFactory);
        return successHandler;
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

        @Bean
        FilterRegistrationBean<JwtAccessFilter> accessJwtFilterRegistrationBean(JwtAccessFilter jwtAccessFilter) {
        FilterRegistrationBean<JwtAccessFilter> frb = new FilterRegistrationBean<>(jwtAccessFilter);
        frb.setEnabled(false);
        return frb;
    }
}
