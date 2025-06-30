package com.example.rd.autocode.assessment.appliances.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    @Mapping(target = "roleName", expression = "java(user.getClass().getSimpleName())")
    UserRecord toDto(User user);
    List<UserRecord> toDto(List<User> users);
//    @Named("mapToRole")
//    default String mapToRole(User value) {
//        return User.class.getSimpleName();
//    }
}
