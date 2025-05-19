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
public class Appliance {
    Long id; String name; Category category; String model; Manufacturer manufacturer; PowerType powerType; String characteristic; String description; Integer power; BigDecimal price;
}
