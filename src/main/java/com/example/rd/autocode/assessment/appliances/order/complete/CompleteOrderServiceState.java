package com.example.rd.autocode.assessment.appliances.order.complete;

public abstract class CompleteOrderServiceState {

    public abstract void enterLineItem(CompleteOrderService context, Long applianceId, Long number, com.example.rd.autocode.assessment.appliances.order.Order order);

    public abstract void completeOrder(CompleteOrderService context, com.example.rd.autocode.assessment.appliances.order.Order order);


    public abstract void createOrder(CompleteOrderService context, com.example.rd.autocode.assessment.appliances.order.Order order);
}
