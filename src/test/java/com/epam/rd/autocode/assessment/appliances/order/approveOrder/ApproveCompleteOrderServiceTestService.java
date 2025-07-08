package com.example.rd.autocode.assessment.appliances.order.approveOrder;

import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.order.OrderNotFound;
import com.example.rd.autocode.assessment.appliances.order.OrderRepository;
import com.example.rd.autocode.assessment.appliances.order.OrderState;
import com.example.rd.autocode.assessment.appliances.order.approve.ApproveOrderService;
import com.example.rd.autocode.assessment.appliances.order.approve.OrderApprovalDecisionEvent;
import com.example.rd.autocode.assessment.appliances.order.complete.OrderException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ApproveCompleteOrderServiceTestService {

    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    ApplicationEventPublisher eventPublisher = Mockito.mock(ApplicationEventPublisher.class);
    ApproveOrderService service = new ApproveOrderService(orderRepository, eventPublisher);
    Order order = new Order();

    @Test
    void approveShouldSetStateToApprovedWhenOrderIsWaitingForApproval() {
        Long orderId = 1L;
        order.setState(OrderState.WAITING_FOR_APPROVAL);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        service.approve(orderId);

        assertThat(order.getState()).isEqualTo(OrderState.APPROVED);
        verify(eventPublisher, times(1)).publishEvent(any(OrderApprovalDecisionEvent.class));
    }

    @Test
    void approveShouldThrowOrderExceptionWhenOrderIsUncompleted() {
        Long orderId = 1L;
        order.setState(OrderState.WAITING_FOR_COMPLETION);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(OrderException.class, () -> service.approve(orderId));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void approveShouldThrowOrderExceptionWhenOrderIsAlreadyDisapproved() {
        Long orderId = 1L;
        order.setState(OrderState.DISAPPROVED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(OrderException.class, () -> service.approve(orderId));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void approveShouldThrowOrderNotFoundWhenOrderDoesNotExist() {
        Long orderId = 99L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFound.class, () -> service.approve(orderId));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void disapproveShouldSetStateToDisapprovedWhenOrderIsWaitingForApproval() {
        Long orderId = 1L;
        order.setState(OrderState.WAITING_FOR_APPROVAL);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        service.disapprove(orderId);

        assertThat(order.getState()).isEqualTo(OrderState.DISAPPROVED);
        verify(eventPublisher, times(1)).publishEvent(any(OrderApprovalDecisionEvent.class));
    }

    @Test
    void disapproveShouldThrowOrderExceptionWhenOrderIsUncompleted() {
        Long orderId = 1L;
        order.setState(OrderState.WAITING_FOR_COMPLETION);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(OrderException.class, () -> service.disapprove(orderId));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void disapproveShouldThrowOrderExceptionWhenOrderIsAlreadyApproved() {
        Long orderId = 1L;
        order.setState(OrderState.APPROVED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(OrderException.class, () -> service.disapprove(orderId));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void disapproveShouldThrowOrderNotFoundWhenOrderDoesNotExist() {
        Long orderId = 99L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFound.class, () -> service.disapprove(orderId));
        verify(eventPublisher, never()).publishEvent(any());
    }
}