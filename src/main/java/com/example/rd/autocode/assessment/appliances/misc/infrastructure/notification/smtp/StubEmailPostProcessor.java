package com.example.rd.autocode.assessment.appliances.misc.infrastructure.notification.smtp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StubEmailPostProcessor implements EmailPostProcessor {
    @Value("${spring.mail.username}")
    String stub;
    @Override
    public String postProcess(String email) {
        return stub;
    }
}
