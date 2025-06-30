package com.example.rd.autocode.assessment.appliances.order.complete;

import com.example.rd.autocode.assessment.appliances.appliance.find.ApplianceNotFound;
import com.example.rd.autocode.assessment.appliances.order.Order;
import org.springframework.stereotype.Component;

@Component
public class WaitingForCompletionState extends CompleteOrderServiceState {

    @Override
    public void enterLineItem(CompleteOrderService context, Long applianceId, Long number) {
        context.getOrder().addRow(context.getApplianceRepository().findById(applianceId).orElseThrow(ApplianceNotFound::new), number);
    }

    @Override
    public void completeOrder(CompleteOrderService context) {
        context.getOrder().complete();
        Order saved = context.getOrderRepository().save(context.getOrder());
        context.getEventPublisher().publishEvent(new OrderCompleted(saved));
    }

    @Override
    public void createOrder(CompleteOrderService context) {
        throw new OrderException("You can not create new order because you are having pending one");
    }


}
