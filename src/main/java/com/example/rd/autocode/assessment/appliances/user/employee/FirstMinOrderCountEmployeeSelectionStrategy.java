package com.example.rd.autocode.assessment.appliances.user.employee;

import com.example.rd.autocode.assessment.appliances.user.employee.signUp.Employee;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class FirstMinOrderCountEmployeeSelectionStrategy implements EmployeeSelectionStrategy {
    private final EmployeeRepository employeeRepository;
    @Override
    public Optional<Employee> select() {
        return employeeRepository.findFirstWithMinOrderCount();
    }
}
