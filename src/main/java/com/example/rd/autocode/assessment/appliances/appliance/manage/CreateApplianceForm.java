package com.example.rd.autocode.assessment.appliances.appliance.manage;

import com.example.rd.autocode.assessment.appliances.appliance.Category;
import com.example.rd.autocode.assessment.appliances.appliance.PowerType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateApplianceForm {
    @NotBlank
    @Size(min = 2, max = 20)
    String name;
    @NotNull
    Category category;
    @NotBlank
    @Size(min = 2, max = 20)
    String model;
    @NotNull
    PowerType powerType;
    @NotNull
    Long manufacturer;
    @NotBlank
    @Size(min = 2, max = 200)
    String characteristic;
    @NotBlank
    @Size(min = 2, max = 200)
    String description;
    @NotNull
    @Min(value = 0)
    Integer power;
    @NotNull
    @Min(value = 0)
    BigDecimal price;

    public CreateApplianceParameters toParameters() {
        return new CreateApplianceParameters(name, category, model, powerType, manufacturer, characteristic, description, power, price);
    }
}
