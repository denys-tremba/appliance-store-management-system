package com.example.rd.autocode.assessment.appliances.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Employee extends User{
    String department;

    public Employee() {
    }

    public Employee(Long id, String name, String email, String password, String department) {
        super(id, name, email, password);
        this.department = department;
    }
}
