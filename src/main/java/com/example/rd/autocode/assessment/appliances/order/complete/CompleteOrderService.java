package com.example.rd.autocode.assessment.appliances.order.complete;


import com.example.rd.autocode.assessment.appliances.appliance.ApplianceRepository;
import com.example.rd.autocode.assessment.appliances.order.OrderNotFound;
import com.example.rd.autocode.assessment.appliances.order.OrderRepository;
import com.example.rd.autocode.assessment.appliances.user.client.signUp.ClientRepository;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.EmployeeRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
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
    CompleteOrderServiceState delegate;

    @Autowired
    @Qualifier("waitingForOrderCreationState")
    public void setState(CompleteOrderServiceState state) {
        delegate = state;
    }

    @Transactional
    public void enterLineItem(Long applianceId, Long number, com.example.rd.autocode.assessment.appliances.order.Order order) {
        delegate.enterLineItem(this,applianceId, number, order);
    }

    public com.example.rd.autocode.assessment.appliances.order.Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFound::new);
    }

    @Transactional
    public void completeOrder(com.example.rd.autocode.assessment.appliances.order.Order order) {
        delegate.completeOrder(this, order);
    }
}
