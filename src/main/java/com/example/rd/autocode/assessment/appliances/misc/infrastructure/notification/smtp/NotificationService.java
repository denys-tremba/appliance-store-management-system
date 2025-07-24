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
    EmailPostProcessor emailPostProcessor;


    @Async
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @EventListener(OrderApprovalDecisionEvent.class)
    public void onCompletion(OrderApprovalDecisionEvent event) {
        Order order = event.order();
        String templateName = event.isApproved() ? "email/approval" : "email/disapproval";
        String orderApproval = event.isApproved() ? "Order approval" : "Order disapproval";
        SimpleMailMessage message = createMailMessage(order.getClient().getEmail(), order, templateName, orderApproval);
        mailSender.send(message);
    }

    @Async
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @EventListener(OrderCompleted.class)
    public void onCompletion(OrderCompleted event) {
        Order order = event.order();
        SimpleMailMessage message = createMailMessage(order.getEmployee().getEmail(), order, "email/complete", "Order completion");
        mailSender.send(message);
    }

    private SimpleMailMessage createMailMessage(String to, Order order, String templateName, String subject) {
        Context context = new Context();
        context.setVariable("order", order);
        String body = templateEngine.process(templateName, context);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setTo(emailPostProcessor.postProcess(to));
        message.setText(body);
        return message;
    }
}
