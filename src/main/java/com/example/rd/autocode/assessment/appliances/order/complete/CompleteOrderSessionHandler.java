package com.example.rd.autocode.assessment.appliances.order.complete;

import com.example.rd.autocode.assessment.appliances.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompleteOrderSessionHandler {
    private Order order = new NullOrder();
    @JsonDeserialize(as = CompleteOrderSessionHandlerStates.class)
    private CompleteOrderSessionHandlerState state = CompleteOrderSessionHandlerStates.WAITING_FOR_ORDER_CREATION;
    @JsonIgnore
    private CompleteOrderService completeOrderService;




    public void clearOrder() {
        order.clear();
    }

    public void removeLineItemAt(int index) {
        order.removeLineItemAt(index);
    }

    public void completeOrder() {
        state.completeOrder(this, completeOrderService);
    }

    public void enterLineItem(Long applianceId, Long quantity) {
        state.enterLineItem(this, completeOrderService, applianceId, quantity);
    }

    public void updateLineItemQuantity(int index, Long quantity) {
        order.updateLineItemQuantity(index, quantity);
    }

    public void removeOrderLineItemsWithApplianceId(Long applianceId) {
        order.getOrderLineItems().removeIf(li -> li.getAppliance().getId().equals(applianceId));
    }

    public void revoke(Long id) {
        completeOrderService.revoke(id);
    }
}
