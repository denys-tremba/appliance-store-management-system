package com.example.rd.autocode.assessment.appliances.auxiliary;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.order.OrderLineItem;
import com.example.rd.autocode.assessment.appliances.order.OrderState;
import com.example.rd.autocode.assessment.appliances.user.client.signUp.Client;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.Employee;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrdersBuilder {
    Long id = 0L;
    Employee employee;
    Client client;
    List<OrderLineItem> orderLineItemSet = new ArrayList<>();
    Boolean approved = false;
    OrderState state = OrderState.WAITING_FOR_COMPLETION;
    ApplianceBuilder applianceBuilder;

    public OrdersBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OrdersBuilder employee(Employee employee) {
        this.employee = employee;
        return this;
    }
    public OrdersBuilder client(Client client) {
        this.client = client;
        return this;
    }
    public OrdersBuilder row(Appliance appliance, Long number) {
        orderLineItemSet.add(OrderLineItem.create(appliance, number));
        return this;
    }
    public OrdersBuilder lineItemWithPrice(BigDecimal price) {
        applianceBuilder = new ApplianceBuilder().price(price);
        return this;
    }

    public OrdersBuilder withQuantity(Long quantity) {
        orderLineItemSet.add(OrderLineItem.create(applianceBuilder.build(), quantity));
        return this;
    }

    public OrdersBuilder approved() {
        this.approved = true;
        return this;
    }

    public OrdersBuilder disapproved() {
        this.approved = false;
        return this;
    }
    public OrdersBuilder state(OrderState state) {
        this.state = state;
        return this;
    }

    public Order build() {
        return new Order(id, employee, client, orderLineItemSet, state, Instant.now());
    }
}
