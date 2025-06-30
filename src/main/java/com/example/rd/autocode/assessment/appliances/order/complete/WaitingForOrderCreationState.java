package com.example.rd.autocode.assessment.appliances.order.complete;

import com.example.rd.autocode.assessment.appliances.appliance.find.ApplianceNotFound;
import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.user.client.signUp.ClientNotFound;
import com.example.rd.autocode.assessment.appliances.user.employee.EmployeeSelectionStrategy;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.EmployeeNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingForOrderCreationState extends CompleteOrderServiceState {
    private final WaitingForCompletionState waitingForCompletionState;
    private final EmployeeSelectionStrategy selectionStrategy;
    @Override
    public void enterLineItem(CompleteOrderService context, Long applianceId, Long number) {
        createOrder(context);
        context.getOrder().addRow(context.getApplianceRepository().findById(applianceId).orElseThrow(ApplianceNotFound::new), number);
    }

    @Override
    public void completeOrder(CompleteOrderService context) {
        throw new OrderException("You can not complete empty order");
    }

    @Override
    public void createOrder(CompleteOrderService context) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!context.getOrderRepository().findPendingOrders(username).isEmpty()) {
            throw new OrderException("You can not create new order as you are having pending one");
        }

        context.setOrder(new Order());
        context.getOrder().setClient(context.getClientRepository().findByEmail(username).orElseThrow(ClientNotFound::new));
        context.getOrder().setEmployee(selectionStrategy.select().orElseThrow(EmployeeNotFound::new));
        context.setState(waitingForCompletionState);
    }
}
