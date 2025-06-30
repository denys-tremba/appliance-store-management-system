package com.example.rd.autocode.assessment.appliances.misc.infrastructure.logging;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BusinessLogicLoggingListener {
    @Async
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @EventListener(BusinessLogicEvent.class)
    public void onEvent(BusinessLogicEvent event) {
        log.info(event.describe());
    }
}
