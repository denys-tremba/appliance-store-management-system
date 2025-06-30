package com.example.rd.autocode.assessment.appliances.order.complete;

import com.example.rd.autocode.assessment.appliances.appliance.find.ApplianceNotFound;
import org.springframework.stereotype.Component;

@Component
public class WaitingForCompletionState extends CompleteOrderServiceState {

    @Override
    public void enterLineItem(CompleteOrderService context, Long applianceId, Long number, com.example.rd.autocode.assessment.appliances.order.Order order) {
        order.addRow(context.getApplianceRepository().findById(applianceId).orElseThrow(ApplianceNotFound::new), number);
    }

    @Override
    public void completeOrder(CompleteOrderService context, com.example.rd.autocode.assessment.appliances.order.Order order) {
        order.complete();
        com.example.rd.autocode.assessment.appliances.order.Order saved = context.getOrderRepository().save(order);
        order = new NullOrder();
        context.getEventPublisher().publishEvent(new OrderCompleted(saved));
    }

    @Override
    public void createOrder(CompleteOrderService context, com.example.rd.autocode.assessment.appliances.order.Order order) {
        throw new OrderException("You can not create new order because you are having pending one");
    }


}
