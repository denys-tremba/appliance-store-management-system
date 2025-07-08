package com.example.rd.autocode.assessment.appliances.order.complete;


import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.appliance.ApplianceRepository;
import com.example.rd.autocode.assessment.appliances.appliance.find.ApplianceNotFound;
import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.order.OrderNotFound;
import com.example.rd.autocode.assessment.appliances.order.OrderRepository;
import com.example.rd.autocode.assessment.appliances.user.client.signUp.Client;
import com.example.rd.autocode.assessment.appliances.user.client.signUp.ClientNotFound;
import com.example.rd.autocode.assessment.appliances.user.client.signUp.ClientRepository;
import com.example.rd.autocode.assessment.appliances.user.employee.EmployeeSelectionStrategy;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.Employee;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.EmployeeNotFound;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompleteOrderService {
    final ClientRepository clientRepository;
    final EmployeeRepository employeeRepository;
    final ApplianceRepository applianceRepository;
    final OrderRepository orderRepository;
    final ApplicationEventPublisher eventPublisher;
    final EmployeeSelectionStrategy employeeSelectionStrategy;
    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public Order create() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!orderRepository.findPendingOrders(username).isEmpty()) {
            throw new OrderException("You can not create new order as you are having pending one");
        }
        Client client = clientRepository.findByEmail(username).orElseThrow(ClientNotFound::new);
        Employee employee = employeeSelectionStrategy.select().orElseThrow(EmployeeNotFound::new);
        Order order = new Order();
        order.setClient(client);
        order.setEmployee(employee);
        return orderRepository.save(order);
    }
    @Transactional
    public void enterLineItem(Long applianceId, Long quantity, com.example.rd.autocode.assessment.appliances.order.Order order) {
        Appliance appliance = applianceRepository.findById(applianceId).orElseThrow(ApplianceNotFound::new);
        order.enterLineItem(appliance, quantity);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFound::new);
    }

    @Transactional
    public void completeOrder(Order order) {
//        Order managed = entityManager.merge(order);
//        Order fetched = orderRepository.findById(order.getId()).orElseThrow(OrderNotFound::new);
//        managed.complete();
//        orderRepository.save(order);
        order.complete();
        Order managed = orderRepository.save(order);
        eventPublisher.publishEvent(new OrderCompleted(managed));
    }
}
