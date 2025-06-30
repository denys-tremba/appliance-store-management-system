package com.example.rd.autocode.assessment.appliances.order.complete;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class EnterLineItemForm {
    Long ordersId;
    Long applianceId;
    Long numbers;
    BigDecimal price;
}
