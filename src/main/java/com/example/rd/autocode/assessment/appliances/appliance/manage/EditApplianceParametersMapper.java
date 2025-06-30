package com.example.rd.autocode.assessment.appliances.appliance.manage;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EditApplianceParametersMapper {
    EditApplianceParametersMapper INSTANCE = Mappers.getMapper( EditApplianceParametersMapper.class );
    EditApplianceParameters toDto(EditApplianceForm form);
}
