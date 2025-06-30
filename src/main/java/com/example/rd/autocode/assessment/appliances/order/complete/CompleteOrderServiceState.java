package com.example.rd.autocode.assessment.appliances.order.complete;

public abstract class CompleteOrderServiceState {

    public abstract void enterLineItem(CompleteOrderService context, Long applianceId, Long number);

    public abstract void completeOrder(CompleteOrderService context);


    public abstract void createOrder(CompleteOrderService context);
}
