package com.example.rd.autocode.assessment.appliances.order.approve;

import com.example.rd.autocode.assessment.appliances.order.OrderState;

public record OrderRecord(String id, String client, String employee, OrderState state, String amount, String creationDate, String creationTime) {
}
