package com.example.rd.autocode.assessment.appliances.order;

import com.example.rd.autocode.assessment.appliances.auxiliary.OrdersBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.valueOf;

class CompleteOrderServiceTest {

    @Test
    void totalIsEqualToSumOfLineItemSubtotals() {
        Order order = new OrdersBuilder()
                .lineItemWithPrice(valueOf(2)).withQuantity(3L)
                .lineItemWithPrice(valueOf(5)).withQuantity(7L)
                .build();

        Assertions.assertThat(order.getAmount()).isEqualTo(valueOf(41));
    }
}