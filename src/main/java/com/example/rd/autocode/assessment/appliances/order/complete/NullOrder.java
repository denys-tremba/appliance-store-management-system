package com.example.rd.autocode.assessment.appliances.order.complete;

import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.order.OrderState;

import java.util.Collections;

public class NullOrder extends Order {
    NullOrder() {
        super(Long.valueOf(0), null, null, Collections.emptyList(), OrderState.WAITING_FOR_COMPLETION, null);
    }
}
