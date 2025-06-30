package com.example.rd.autocode.assessment.appliances.misc.infrastructure.notification.smtp;

import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.order.approve.OrderApprovalDecisionEvent;
import com.example.rd.autocode.assessment.appliances.order.complete.OrderCompleted;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
    TemplateEngine templateEngine;
    JavaMailSender mailSender;


    @Async
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @EventListener(OrderApprovalDecisionEvent.class)
    public void onCompletion(OrderApprovalDecisionEvent event) {
        SimpleMailMessage message = createMailMessage(event.order(), "email/approval", "Order approval");
        mailSender.send(message);
    }

    @Async
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @EventListener(OrderCompleted.class)
    public void onCompletion(OrderCompleted event) {
        SimpleMailMessage message = createMailMessage(event.order(), "email/complete", "Order completion");
        mailSender.send(message);
    }

    private SimpleMailMessage createMailMessage(Order order, String templateName, String subject) {
        Context context = new Context();
        context.setVariable("order", order);
        String body = templateEngine.process(templateName, context);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setTo("denys.tremba.trying@gmail.com");
        message.setText(body);
        return message;
    }
}
