package com.example.rd.autocode.assessment.appliances.manufacturer.manage;

import lombok.Data;

@Data
public class EditManufacturerForm {
    Long id;
    String name;

    public static EditManufacturerForm from(Manufacturer manufacturer) {
        EditManufacturerForm form = new EditManufacturerForm();
        form.setName(manufacturer.getName());
        form.setId(manufacturer.getId());
        return form;
    }
}
