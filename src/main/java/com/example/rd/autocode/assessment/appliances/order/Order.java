package com.example.rd.autocode.assessment.appliances.order;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.order.complete.OrderException;
import com.example.rd.autocode.assessment.appliances.user.client.signUp.Client;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    Employee employee;
    @ManyToOne
    Client client;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_line_items", joinColumns = @JoinColumn(name = "order_id"))
    List<OrderLineItem> orderLineItems = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    OrderState state = OrderState.WAITING_FOR_COMPLETION;
    @CreationTimestamp
    Instant createdAt;

    public static Order create(Employee employee, Client client) {
        Order order = new Order();
        order.setEmployee(employee);
        order.setClient(client);
        return order;
    }


    public BigDecimal getAmount() {
        return orderLineItems.stream().map(OrderLineItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addRow(Appliance appliance, Long number) {
        if (state.equals(OrderState.WAITING_FOR_APPROVAL)) {
            throw new OrderException("Adding line item is failed due to order is completed");
        }
        orderLineItems.add(OrderLineItem.create(appliance, number));
    }

    public void complete() {
        if (orderLineItems.isEmpty()) {
            throw new OrderException("You can not complete empty order");
        }
        state = OrderState.WAITING_FOR_APPROVAL;
    }

    public void approve() {
        if (state.equals(OrderState.WAITING_FOR_COMPLETION)) {
            throw new OrderException("You can not approve uncompleted order");
        }
        if (state.equals(OrderState.DISAPPROVED)) {
            throw new OrderException("You can not approve already disapproved order");
        }



        state = OrderState.APPROVED;
    }

    public void disapprove() {
        if (state.equals(OrderState.WAITING_FOR_COMPLETION)) {
            throw new OrderException("You can not disapprove uncompleted order");
        }
        if (state.equals(OrderState.APPROVED)) {
            throw new OrderException("You can not disapprove already approved order");
        }


        state = OrderState.DISAPPROVED;
    }
}
