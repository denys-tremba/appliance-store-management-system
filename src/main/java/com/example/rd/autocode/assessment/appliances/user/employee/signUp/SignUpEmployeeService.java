package com.example.rd.autocode.assessment.appliances.user.employee.signUp;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SignUpEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    public void save(String name, String email, String password, String department) {
        Employee employee = Employee.create(name, email, passwordEncoder.encode(password), department);
        employeeRepository.save(employee);
        eventPublisher.publishEvent(new EmployeeRegistered(employee));
    }
}
