package com.example.rd.autocode.assessment.appliances.user.employee.signUp;

import com.example.rd.autocode.assessment.appliances.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString(callSuper = true)
@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Entity
@DiscriminatorValue("employee")
public class Employee extends User {
    @Column(length = 32)
    String department;

    public Employee(Long id, String name, String email, String password, String department, Boolean locked) {
        super(id, name, email, password, locked);
        this.department = department;
    }

    public static Employee create(String name, String email, String password, String department) {
        return new Employee(null, name, email, password, department, false);
    }
}
