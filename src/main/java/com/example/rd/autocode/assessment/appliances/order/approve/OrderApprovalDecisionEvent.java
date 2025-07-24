package com.example.rd.autocode.assessment.appliances.order.approve;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.logging.BusinessLogicEvent;
import com.example.rd.autocode.assessment.appliances.order.Order;

public record OrderApprovalDecisionEvent(Order order, boolean isApproved) implements BusinessLogicEvent {
    @Override
    public String describe() {
        return "Order #%s is %sapproved by %s".formatted(order.getId(), isApproved?"":"dis", order.getEmployee());
    }
}
