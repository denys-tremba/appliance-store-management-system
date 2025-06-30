package com.example.rd.autocode.assessment.appliances.manufacturer.manage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class ManufacturerParameters {
    Long id; String name;

    public Manufacturer toManufacturer() {
        return new Manufacturer(id, name);
    }
}
