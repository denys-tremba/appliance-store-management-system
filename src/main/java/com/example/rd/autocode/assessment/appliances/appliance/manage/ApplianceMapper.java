package com.example.rd.autocode.assessment.appliances.appliance.manage;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.Manufacturer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApplianceMapper {
    ApplianceMapper INSTANCE = Mappers.getMapper( ApplianceMapper.class );
    EditApplianceForm toDto(Appliance appliance);
    default Long map(Manufacturer value) {
        return value.getId();
    }
}
