package com.example.rd.autocode.assessment.appliances.user.employee.signUp;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.logging.BusinessLogicEvent;

public record EmployeeRegistered(Employee employee) implements BusinessLogicEvent {
    @Override
    public String describe() {
        return "Employee %s [%s] from department %s registered himself".formatted(employee.getName(), employee.getEmail(), employee.getDepartment());
    }
}
