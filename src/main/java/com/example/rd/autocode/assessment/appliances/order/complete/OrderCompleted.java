package com.example.rd.autocode.assessment.appliances.order.complete;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.logging.BusinessLogicEvent;
import com.example.rd.autocode.assessment.appliances.order.Order;

public record OrderCompleted(Order order) implements BusinessLogicEvent {
    @Override
    public String describe() {
        return "Order #%s completed by %s for %s with total %s containing %s".formatted(order.getId(), order.getClient(), order.getEmployee(), order.getAmount(), order.getOrderLineItems());
    }
}
