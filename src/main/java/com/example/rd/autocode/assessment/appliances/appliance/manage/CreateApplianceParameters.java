package com.example.rd.autocode.assessment.appliances.appliance.manage;

import com.example.rd.autocode.assessment.appliances.appliance.Category;
import com.example.rd.autocode.assessment.appliances.appliance.PowerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@Builder
public class CreateApplianceParameters {
    String name;
    Category category;
    String model;
    PowerType powerType;
    Long manufacturerId;
    String characteristic;
    String description;
    Integer power;
    BigDecimal price;
}
