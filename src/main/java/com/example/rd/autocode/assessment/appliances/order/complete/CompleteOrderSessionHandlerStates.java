package com.example.rd.autocode.assessment.appliances.order.complete;

import com.example.rd.autocode.assessment.appliances.order.Order;

public enum CompleteOrderSessionHandlerStates implements CompleteOrderSessionHandlerState{
    WAITING_FOR_ORDER_CREATION {
        @Override
        public void enterLineItem(CompleteOrderSessionHandler sessionHandler, CompleteOrderService completeOrderService, Long applianceId, Long quantity) {
            Order order = completeOrderService.create();
            sessionHandler.setOrder(order);
            sessionHandler.setState(WAITING_FOR_ORDER_COMPLETION);
            completeOrderService.enterLineItem(applianceId, quantity, order);
        }

        @Override
        public void completeOrder(CompleteOrderSessionHandler sessionHandler, CompleteOrderService completeOrderService) {
            throw new OrderException("You can not complete empty order");
        }
    },
    WAITING_FOR_ORDER_COMPLETION {
        @Override
        public void enterLineItem(CompleteOrderSessionHandler sessionHandler, CompleteOrderService completeOrderService, Long applianceId, Long quantity) {
            completeOrderService.enterLineItem(applianceId, quantity, sessionHandler.getOrder());
        }

        @Override
        public void completeOrder(CompleteOrderSessionHandler sessionHandler, CompleteOrderService completeOrderService) {
            completeOrderService.completeOrder(sessionHandler.getOrder());
            sessionHandler.setOrder(new NullOrder());
            sessionHandler.setState(WAITING_FOR_ORDER_CREATION);
        }
    }
}
