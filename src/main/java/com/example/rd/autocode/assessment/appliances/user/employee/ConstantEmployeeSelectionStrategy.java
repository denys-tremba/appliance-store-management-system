package com.example.rd.autocode.assessment.appliances.user.employee;

import com.example.rd.autocode.assessment.appliances.user.employee.signUp.Employee;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Optional;
@RequiredArgsConstructor
@Component
public class ConstantEmployeeSelectionStrategy implements EmployeeSelectionStrategy {
    private final EmployeeRepository employeeRepository;
    @Setter
    private Employee employee;

    @Override
    public Optional<Employee> select() {
        return Optional.of(employee);
    }
}
