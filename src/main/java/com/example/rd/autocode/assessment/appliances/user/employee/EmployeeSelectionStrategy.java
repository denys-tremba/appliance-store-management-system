package com.example.rd.autocode.assessment.appliances.user.employee;

import com.example.rd.autocode.assessment.appliances.user.employee.signUp.Employee;

import java.util.Optional;

public interface EmployeeSelectionStrategy {
    Optional<Employee> select();
}
