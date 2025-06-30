package com.example.rd.autocode.assessment.appliances.appliance.manage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EditApplianceForm extends CreateApplianceForm {
    long version;
    Long id;
}
