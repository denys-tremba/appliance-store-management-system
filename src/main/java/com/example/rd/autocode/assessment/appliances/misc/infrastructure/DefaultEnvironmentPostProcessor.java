package com.example.rd.autocode.assessment.appliances.misc.infrastructure;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

public class DefaultEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private final PropertiesPropertySourceLoader loader = new PropertiesPropertySourceLoader();

    @SneakyThrows
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        ClassPathResource resource = new ClassPathResource(".env");
        PropertySource<?> propertySource = loader.load("env resource", resource).get(0);
        environment.getPropertySources().addFirst(propertySource);
    }
}
