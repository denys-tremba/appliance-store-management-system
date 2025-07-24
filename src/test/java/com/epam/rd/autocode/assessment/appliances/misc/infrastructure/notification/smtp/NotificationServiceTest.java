package com.example.rd.autocode.assessment.appliances.misc.infrastructure.notification.smtp;

import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.order.approve.OrderApprovalDecisionEvent;
import com.example.rd.autocode.assessment.appliances.order.complete.OrderCompleted;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Disabled
class NotificationServiceTest {

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    @DisplayName("Should send approval email when an OrderApprovalDecisionEvent is received")
    void onCompletionWithApprovalEventShouldSendApprovalEmail() {
        // Arrange
        Order mockOrder = new Order(); // A mock or real Order object
        OrderApprovalDecisionEvent event = new OrderApprovalDecisionEvent(mockOrder, false);
        String expectedEmailBody = "This is a processed approval email.";
        String expectedTemplateName = "email/approval";
        String expectedSubject = "Order approval";

        // Mock the behavior of the template engine
        when(templateEngine.process(eq(expectedTemplateName), any(Context.class)))
                .thenReturn(expectedEmailBody);

        // Act
        // We call the event listener method directly to test its logic
        notificationService.onCompletion(event);

        // Assert
        // 1. Capture the message sent to the mail sender
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        // 2. Verify the captured message has the correct content
        assertNotNull(capturedMessage);
        assertEquals(expectedSubject, capturedMessage.getSubject());
        assertEquals(expectedEmailBody, capturedMessage.getText());
        assertNotNull(capturedMessage.getTo());
        assertEquals(1, capturedMessage.getTo().length);
        assertEquals("denys.tremba.trying@gmail.com", capturedMessage.getTo()[0]);

        // 3. Verify the template engine was called with the correct parameters
        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine).process(eq(expectedTemplateName), contextCaptor.capture());
        assertEquals(mockOrder, contextCaptor.getValue().getVariable("order"));
    }

    @Test
    @DisplayName("Should send completion email when an OrderCompleted event is received")
    void onCompletionWithCompletedEventShouldSendCompletionEmail() {
        // Arrange
        Order mockOrder = new Order();
        OrderCompleted event = new OrderCompleted(mockOrder);
        String expectedEmailBody = "This is a processed completion email.";
        String expectedTemplateName = "email/complete";
        String expectedSubject = "Order completion";

        // Mock the behavior of the template engine
        when(templateEngine.process(eq(expectedTemplateName), any(Context.class)))
                .thenReturn(expectedEmailBody);

        // Act
        notificationService.onCompletion(event);

        // Assert
        // 1. Capture the message sent to the mail sender
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        // 2. Verify the captured message has the correct content
        assertNotNull(capturedMessage);
        assertEquals(expectedSubject, capturedMessage.getSubject());
        assertEquals(expectedEmailBody, capturedMessage.getText());
        assertNotNull(capturedMessage.getTo());
        assertEquals("denys.tremba.trying@gmail.com", capturedMessage.getTo()[0]);

        // 3. Verify the template engine was called correctly
        verify(templateEngine).process(eq(expectedTemplateName), any(Context.class));
    }

    @Test
    @DisplayName("Should not send email if template processing fails")
    void onCompletionShouldNotSendEmailWhenTemplateProcessingFails() {
        // Arrange
        Order mockOrder = new Order();
        OrderCompleted event = new OrderCompleted(mockOrder);

        // Mock the template engine to throw an exception
        when(templateEngine.process(anyString(), any(Context.class)))
                .thenThrow(new RuntimeException("Thymeleaf processing error"));

        // Act & Assert
        // Check that the exception propagates up
        assertThrows(RuntimeException.class, () -> {
            notificationService.onCompletion(event);
        });

        // Crucially, verify that mailSender.send() was never called
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }
}