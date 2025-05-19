package com.example.rd.autocode.assessment.appliances.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class OrderRow {
    Long id; Appliance appliance; Long number; BigDecimal amount;
}
