package com.example.rd.autocode.assessment.appliances.order.complete;

public interface CompleteOrderSessionHandlerState {
    void enterLineItem(CompleteOrderSessionHandler sessionHandler, CompleteOrderService completeOrderService, Long applianceId, Long quantity);

    void completeOrder(CompleteOrderSessionHandler sessionHandler, CompleteOrderService completeOrderService);
}
