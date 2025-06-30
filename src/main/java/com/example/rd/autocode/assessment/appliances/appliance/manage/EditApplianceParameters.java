package com.example.rd.autocode.assessment.appliances.appliance.manage;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.appliance.Category;
import com.example.rd.autocode.assessment.appliances.appliance.PowerType;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.Manufacturer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class EditApplianceParameters {
    String name;
    Category category;
    String model;
    PowerType powerType;
    Long manufacturer;
    String characteristic;
    String description;
    Integer power;
    BigDecimal price;
    long version;
    Long id;

    public void update(Appliance appliance, Manufacturer manufacturer) {
        appliance.setName(name);
        appliance.setCategory(category);
        appliance.setModel(model);
        appliance.setPowerType(powerType);
        appliance.setManufacturer(manufacturer);
        appliance.setCharacteristic(characteristic);
        appliance.setDescription(description);
        appliance.setPower(power);
        appliance.setPrice(price);
        appliance.setVersion(version);
    }
}
