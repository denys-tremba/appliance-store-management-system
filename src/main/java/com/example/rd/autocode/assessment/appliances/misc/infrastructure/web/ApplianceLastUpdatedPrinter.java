package com.example.rd.autocode.assessment.appliances.misc.infrastructure.web;

import org.springframework.format.Printer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
@Component
public class ApplianceLastUpdatedPrinter implements Printer<Instant> {

    @Override
    public String print(Instant object, Locale locale) {
        long minutes = Duration.between(object, Instant.now()).toMinutes();
        return String.valueOf(minutes);
    }
}
