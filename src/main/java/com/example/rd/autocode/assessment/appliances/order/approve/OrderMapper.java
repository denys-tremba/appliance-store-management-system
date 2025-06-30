package com.example.rd.autocode.assessment.appliances.order.approve;

import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.user.User;
import com.example.rd.autocode.assessment.appliances.user.client.signUp.Client;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper( OrderMapper.class );

    @Mapping(target = "creationDate", source = "createdAt", qualifiedByName = "mapToDate")
    @Mapping(target = "creationTime", source = "createdAt", qualifiedByName = "mapToTime")

    OrderRecord toDto(Order order);
    List<OrderRecord> toDto(List<Order> orders);
    default String map(Client value) {
        return Objects.requireNonNullElse(value, User.ANONYMOUS).getName();
    }
    default String map(Employee value) {
        return Objects.requireNonNullElse(value, User.ANONYMOUS).getName();
    }
    @Named("mapToDate")
    default String mapToDate(Instant value) {
        if (value == null) {
            return "Unknown";
        }

        return LocalDate.ofInstant(value, ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE);
    }
    @Named("mapToTime")
    default String mapToTime(Instant value) {
        if (value == null) {
            return "Unknown";
        }

        return LocalTime.ofInstant(value, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
