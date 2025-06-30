package com.example.rd.autocode.assessment.appliances.order;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Embeddable
public class OrderLineItem {

    @ManyToOne
    Appliance appliance;
    Long quantity;
    BigDecimal price;

    public static OrderLineItem create(Appliance appliance, Long quantity) {
        return new OrderLineItem(appliance, quantity, appliance.getPrice());
    }

    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
