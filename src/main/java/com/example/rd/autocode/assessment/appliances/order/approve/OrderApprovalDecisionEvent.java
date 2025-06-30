package com.example.rd.autocode.assessment.appliances.order.approve;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.logging.BusinessLogicEvent;
import com.example.rd.autocode.assessment.appliances.order.Order;

public record OrderApprovalDecisionEvent(Order order) implements BusinessLogicEvent {
    @Override
    public String describe() {
        return "Order #%s is approved by %s".formatted(order.getId(), order.getEmployee());
    }
}
