package com.example.rd.autocode.assessment.appliances.order.completeOrder;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.appliance.ApplianceRepository;
import com.example.rd.autocode.assessment.appliances.appliance.find.ApplianceNotFound;
import com.example.rd.autocode.assessment.appliances.auxiliary.ApplianceBuilder;
import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.order.OrderNotFound;
import com.example.rd.autocode.assessment.appliances.order.OrderRepository;
import com.example.rd.autocode.assessment.appliances.order.OrderState;
import com.example.rd.autocode.assessment.appliances.order.complete.CompleteOrderService;
import com.example.rd.autocode.assessment.appliances.order.complete.OrderCompleted;
import com.example.rd.autocode.assessment.appliances.order.complete.OrderException;
import com.example.rd.autocode.assessment.appliances.user.client.signUp.Client;
import com.example.rd.autocode.assessment.appliances.user.client.signUp.ClientNotFound;
import com.example.rd.autocode.assessment.appliances.user.client.signUp.ClientRepository;
import com.example.rd.autocode.assessment.appliances.user.employee.EmployeeSelectionStrategy;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.Employee;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.EmployeeNotFound;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompleteOrderServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EmployeeRepository employeeRepository; // Not used directly, but part of the service
    @Mock
    private ApplianceRepository applianceRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private EmployeeSelectionStrategy employeeSelectionStrategy;

    @InjectMocks
    private CompleteOrderService completeOrderService;

    @Test
    @DisplayName("create() should create and save a new order when user has no pending orders")
    void createWhenUserHasNoPendingOrdersShouldCreateAndSaveNewOrder() {
        // Arrange
        String username = "test@client.com";
        Client mockClient = new Client();
        Employee mockEmployee = new Employee();
        Order savedOrder = new Order();
        savedOrder.setId(1L);

        // Mocking the static SecurityContextHolder
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(username);

            // Mocking repository and strategy calls
            when(orderRepository.findPendingOrders(username)).thenReturn(Collections.emptyList());
            when(clientRepository.findByEmail(username)).thenReturn(Optional.of(mockClient));
            when(employeeSelectionStrategy.select()).thenReturn(Optional.of(mockEmployee));
            // Use thenAnswer to return the object that was passed to save
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Order result = completeOrderService.create();

            // Assert
            assertNotNull(result);
            assertEquals(mockClient, result.getClient());
            assertEquals(mockEmployee, result.getEmployee());

            ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
            verify(orderRepository).save(orderCaptor.capture());
            assertEquals(mockClient, orderCaptor.getValue().getClient());
            verify(clientRepository).findByEmail(username);
            verify(employeeSelectionStrategy).select();
        }
    }

    @Test
    @DisplayName("create() should throw OrderException when user has a pending order")
    void createWhenUserHasPendingOrderShouldThrowOrderException() {
        // Arrange
        String username = "test@client.com";
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(username);

            when(orderRepository.findPendingOrders(username)).thenReturn(List.of(new Order()));

            // Act & Assert
            OrderException exception = assertThrows(OrderException.class, () -> completeOrderService.create());
            assertEquals("You can not create new order as you are having pending one", exception.getMessage());
            verify(clientRepository, never()).findByEmail(anyString());
            verify(orderRepository, never()).save(any(Order.class));
        }
    }

    @Test
    @DisplayName("enterLineItem() should add an item to the order when appliance is found")
    void enterLineItemWhenApplianceExistsShouldAddLineItemToOrder() {
        // Arrange
        Long applianceId = 1L;
        Long quantity = 2L;
        Appliance mockAppliance = new Appliance();
        mockAppliance.setId(applianceId);

        Order order = spy(new Order()); // Spy on a real order to verify method call

        when(applianceRepository.findById(applianceId)).thenReturn(Optional.of(mockAppliance));

        // Act
        completeOrderService.enterLineItem(applianceId, quantity, order);

        // Assert
        verify(applianceRepository).findById(applianceId);
        verify(order).enterLineItem(mockAppliance, quantity); // Verify interaction with the spied order
    }

    @Test
    @DisplayName("enterLineItem() should throw ApplianceNotFound when appliance does not exist")
    void enterLineItemWhenApplianceNotFoundShouldThrowApplianceNotFound() {
        // Arrange
        Long applianceId = 99L;
        when(applianceRepository.findById(applianceId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ApplianceNotFound.class, () ->
                completeOrderService.enterLineItem(applianceId, 1L, new Order()));
    }

    @Test
    @DisplayName("findById() should return the order when it exists")
    void findByIdWhenOrderExistsShouldReturnOrder() {
        // Arrange
        Long orderId = 1L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        // Act
        Order result = completeOrderService.findById(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository).findById(orderId);
    }

    @Test
    @DisplayName("findById() should throw OrderNotFound when order does not exist")
    void findByIdWhenOrderDoesNotExistShouldThrowOrderNotFound() {
        // Arrange
        Long orderId = 99L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderNotFound.class, () -> completeOrderService.findById(orderId));
    }

    @Test
    @DisplayName("completeOrder() should call complete, save order, and publish an event")
    void completeOrderShouldSetStatusCompleteAndPublishEvent() {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.enterLineItem(new ApplianceBuilder().build(), 1L);

        // Make save return the passed order
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        completeOrderService.completeOrder(order);

        // Assert
        // 1. Verify the order was 'completed' (assuming complete() sets a status)
        assertEquals(order.getState(), OrderState.WAITING_FOR_APPROVAL); // Assuming complete() sets a boolean flag

        // 2. Verify the order was saved
        verify(orderRepository).save(order);

        // 3. Verify an event was published
        ArgumentCaptor<OrderCompleted> eventCaptor = ArgumentCaptor.forClass(OrderCompleted.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        OrderCompleted publishedEvent = eventCaptor.getValue();
        assertEquals(order, publishedEvent.order());
    }

    // Additional tests for edge cases in create() method
    @Test
    @DisplayName("create() should throw ClientNotFound when client does not exist")
    void createWhenClientNotFoundShouldThrowClientNotFound() {
        String username = "nonexistent@client.com";
        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);
        try (MockedStatic<SecurityContextHolder> mocked = mockStatic(SecurityContextHolder.class)) {
            mocked.when(SecurityContextHolder::getContext).thenReturn(context);
            when(context.getAuthentication()).thenReturn(auth);
            when(auth.getName()).thenReturn(username);
            when(orderRepository.findPendingOrders(username)).thenReturn(Collections.emptyList());
            when(clientRepository.findByEmail(username)).thenReturn(Optional.empty());

            assertThrows(ClientNotFound.class, () -> completeOrderService.create());
        }
    }

    @Test
    @DisplayName("create() should throw EmployeeNotFound when no employee can be selected")
    void createWhenEmployeeNotFoundShouldThrowEmployeeNotFound() {
        String username = "test@client.com";
        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);
        try (MockedStatic<SecurityContextHolder> mocked = mockStatic(SecurityContextHolder.class)) {
            mocked.when(SecurityContextHolder::getContext).thenReturn(context);
            when(context.getAuthentication()).thenReturn(auth);
            when(auth.getName()).thenReturn(username);
            when(orderRepository.findPendingOrders(username)).thenReturn(Collections.emptyList());
            when(clientRepository.findByEmail(username)).thenReturn(Optional.of(new Client()));
            when(employeeSelectionStrategy.select()).thenReturn(Optional.empty());

            assertThrows(EmployeeNotFound.class, () -> completeOrderService.create());
        }
    }
}