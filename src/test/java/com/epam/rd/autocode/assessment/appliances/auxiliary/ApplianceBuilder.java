package com.example.rd.autocode.assessment.appliances.auxiliary;

import com.example.rd.autocode.assessment.appliances.appliance.*;
import com.example.rd.autocode.assessment.appliances.appliance.manage.CreateApplianceParameters;
import com.example.rd.autocode.assessment.appliances.appliance.manage.EditApplianceParameters;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.Manufacturer;

import java.math.BigDecimal;

public class ApplianceBuilder {
    Long id = 0L;
    String name = "";
    Category category = Category.COMPUTING_MOBILE;
    String model = "";
    PowerType powerType = PowerType.ACCUMULATOR;
    Manufacturer manufacturer = new Manufacturer(0L, "m1");
    String characteristic = "";
    String description= "";
    Integer power = 0;
    BigDecimal price = BigDecimal.ZERO;
    public ApplianceBuilder id(Long id) {
        this.id = id;
        return this;
    }
    public ApplianceBuilder name(String name) {
        this.name = name;
        return this;
    }
    public ApplianceBuilder category(Category category) {
        this.category = category;
        return this;
    }
    public ApplianceBuilder model(String model) {
        this.model = model;
        return this;
    }
    public ApplianceBuilder powerType(PowerType powerType) {
        this.powerType = powerType;
        return this;
    }
    public ApplianceBuilder manufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }
    public ApplianceBuilder characteristic(String characteristic) {
        this.characteristic = characteristic;
        return this;
    }
    public ApplianceBuilder description(String description) {
        this.description = description;
        return this;
    }
    public ApplianceBuilder power(Integer power) {
        this.power = power;
        return this;
    }
    public ApplianceBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }
    public Appliance build() {
        return new Appliance(id, name, category, model, powerType, manufacturer, characteristic, description, power, price, null, null, 0);
    }
    public CreateApplianceParameters buildAsParams() {
        return new CreateApplianceParameters(name, category, model, powerType, manufacturer.getId(), characteristic, description, power, price);
    }
    public EditApplianceParameters buildAsEditParams(long version) {
        return new EditApplianceParameters(name, category, model, powerType, manufacturer.getId(), characteristic, description, power, price, version, id);
    }
}
