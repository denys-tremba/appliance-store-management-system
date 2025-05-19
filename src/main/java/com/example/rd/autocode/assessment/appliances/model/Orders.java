package com.example.rd.autocode.assessment.appliances.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Orders {
    Long id; Employee employee; Client client; Set<OrderRow> orderRowSet; Boolean approved;
}
