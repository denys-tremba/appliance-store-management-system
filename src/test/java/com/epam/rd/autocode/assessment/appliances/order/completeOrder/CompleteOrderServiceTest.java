//package com.example.rd.autocode.assessment.appliances.order.completeOrder;
//
//import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
//import com.example.rd.autocode.assessment.appliances.appliance.ApplianceRepository;
//import com.example.rd.autocode.assessment.appliances.order.Order;
//import com.example.rd.autocode.assessment.appliances.order.OrderLineItem;
//import com.example.rd.autocode.assessment.appliances.order.OrderRepository;
//import com.example.rd.autocode.assessment.appliances.order.complete.CompleteOrderService;
//import com.example.rd.autocode.assessment.appliances.order.complete.CompleteOrderServiceState;
//import com.example.rd.autocode.assessment.appliances.order.complete.WaitingForCompletionState;
//import com.example.rd.autocode.assessment.appliances.order.complete.WaitingForOrderCreationState;
//import com.example.rd.autocode.assessment.appliances.user.client.signUp.ClientRepository;
//import com.example.rd.autocode.assessment.appliances.user.employee.signUp.EmployeeRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.context.ApplicationEventPublisher;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class CompleteOrderServiceTest {
//
//    ClientRepository clientRepository = Mockito.mock(ClientRepository.class);
//    EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
//    ApplianceRepository applianceRepository = Mockito.mock(ApplianceRepository.class);
//    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
//    ApplicationEventPublisher eventPublisher = Mockito.mock(ApplicationEventPublisher.class);
//    CompleteOrderService service = new CompleteOrderService(
//            clientRepository, employeeRepository, applianceRepository, orderRepository, eventPublisher
//    );
//
//    CompleteOrderServiceState waitingForCreationState = Mockito.mock(WaitingForOrderCreationState.class);
//    CompleteOrderServiceState waitingForCompletionState = Mockito.mock(WaitingForCompletionState.class);
//
//    @BeforeEach
//    void setup() {
//        service.setOrder(new Order());
//    }
//
//    @Test
//    void enterLineItemShouldDelegateToState() {
//        service.setState(waitingForCreationState);
//        Long applianceId = 1L;
//        Long number = 2L;
//
//        service.enterLineItem(applianceId, number, );
//
//        verify(waitingForCreationState, times(1)).enterLineItem(service, applianceId, number);
//        verify(waitingForCompletionState, never()).enterLineItem(any(), any(), any());
//    }
//
//    @Test
//    void completeOrderShouldDelegateToState() {
//        service.setState(waitingForCompletionState);
//
//        service.completeOrder(order);
//
//        verify(waitingForCompletionState, times(1)).completeOrder(service);
//        verify(waitingForCreationState, never()).completeOrder(any());
//    }
//
//    @Test
//    void clearOrderShouldClearLineItems() {
//        Appliance appliance = new Appliance();
//        service.getCurrentOrder().getOrderLineItems().add(OrderLineItem.create(appliance, 1L));
//
//        service.clearOrder();
//
//        assertThat(service.getCurrentOrder().getOrderLineItems()).isEmpty();
//    }
//
//    @Test
//    void removeOrderLineItemAtShouldRemoveCorrectItem() {
//        Appliance appliance1 = new Appliance();
//        appliance1.setId(1L);
//        Appliance appliance2 = new Appliance();
//        appliance2.setId(2L);
//        service.getCurrentOrder().getOrderLineItems().add(OrderLineItem.create(appliance1, 1L));
//        service.getCurrentOrder().getOrderLineItems().add(OrderLineItem.create(appliance2, 1L));
//
//        service.removeOrderLineItemAt(0);
//
//        assertThat(service.getCurrentOrder().getOrderLineItems()).hasSize(1);
//        assertThat(service.getCurrentOrder().getOrderLineItems().get(0).getAppliance().getId()).isEqualTo(2L);
//    }
//
//    @Test
//    void updateLineItemQuantityShouldUpdateCorrectItem() {
//        Appliance appliance = new Appliance();
//        service.getCurrentOrder().getOrderLineItems().add(OrderLineItem.create(appliance, 1L));
//
//        service.updateLineItemQuantity(0, 5L, );
//
//        assertThat(service.getCurrentOrder().getOrderLineItems().get(0).getQuantity()).isEqualTo(5L);
//    }
//}