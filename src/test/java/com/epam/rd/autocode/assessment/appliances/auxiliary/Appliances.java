package com.example.rd.autocode.assessment.appliances.auxiliary;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.appliance.Category;
import com.example.rd.autocode.assessment.appliances.appliance.manage.CreateApplianceParameters;
import com.example.rd.autocode.assessment.appliances.appliance.PowerType;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.Manufacturer;

import java.math.BigDecimal;

public class Appliances {
    public static Appliance wellFormed() {
        return builder().build();
    }
    public static CreateApplianceParameters asCreateParams() {
        return builder().buildAsParams();
    }

    public static CreateApplianceParameters asEditParams() {
        return builder().buildAsParams();
    }

    public static ApplianceBuilder builder() {
        return new ApplianceBuilder()
                .id(1L)
                .name("name")
                .category(Category.COMPUTING_MOBILE)
                .power(220)
                .description("description")
                .characteristic("characteristic")
                .manufacturer(new Manufacturer(1L, "manufacturer"))
                .model("model")
                .powerType(PowerType.ACCUMULATOR)
                .price(BigDecimal.ONE);
    }
}
