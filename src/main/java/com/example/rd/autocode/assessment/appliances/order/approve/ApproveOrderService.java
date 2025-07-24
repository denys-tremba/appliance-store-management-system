package com.example.rd.autocode.assessment.appliances.order.approve;

import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.order.OrderNotFound;
import com.example.rd.autocode.assessment.appliances.order.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApproveOrderService {
    OrderRepository orderRepository;
    ApplicationEventPublisher eventPublisher;


    public void approve(Long id) {
        Order order = findById(id);
        order.approve();
        eventPublisher.publishEvent(new OrderApprovalDecisionEvent(order, true));
    }


    public void disapprove(Long id) {
        Order order = findById(id);
        order.disapprove();
        eventPublisher.publishEvent(new OrderApprovalDecisionEvent(order, false));
    }



    private Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFound::new);
    }

}
