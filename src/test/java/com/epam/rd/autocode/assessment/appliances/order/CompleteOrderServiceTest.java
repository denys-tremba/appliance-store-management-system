package com.example.rd.autocode.assessment.appliances.order;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.auxiliary.ApplianceBuilder;
import com.example.rd.autocode.assessment.appliances.auxiliary.OrdersBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;

class CompleteOrderServiceTest {

    @Test
    void totalIsEqualToSumOfLineItemSubtotals() {
        Order order = new Order();
        order.enterLineItem(new ApplianceBuilder().price(BigDecimal.valueOf(2)).build(), 3L);
        order.enterLineItem(new ApplianceBuilder().price(BigDecimal.valueOf(5)).build(), 7L);

        Assertions.assertThat(order.getAmount()).isEqualTo(valueOf(41));
    }
}