package com.example.rd.autocode.assessment.appliances.misc.infrastructure;

import com.example.rd.autocode.assessment.appliances.user.client.signUp.Client;
import com.example.rd.autocode.assessment.appliances.user.employee.ConstantEmployeeSelectionStrategy;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.Employee;
import com.example.rd.autocode.assessment.appliances.user.login.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class DbEnricher implements CommandLineRunner, BeanPostProcessor {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final ConstantEmployeeSelectionStrategy constantEmployeeSelectionStrategy;


    @Override
    public void run(String... args) throws Exception {
//        log.error("Error");
        Client charlies = userRepository.save(Client.create("Charlies", "charlies@gmail.com", passwordEncoder.encode("66P[:0NVkWCa"), "4539899931752296"));
        Client bob = userRepository.save(Client.create("Bob", "bob@gmail.com", passwordEncoder.encode("MmX55w1>C0`:"), "4916830689174006"));
        Employee oliver = userRepository.save(Employee.create("Oliver", "oliver@gmail.com", passwordEncoder.encode(">0w.y94Gd6,,"), "Laundry"));
        Employee denys = userRepository.save(Employee.create("Denys", "denys.tremba.trying@gmail.com", passwordEncoder.encode("5V38&62e!Lv^"), "Air Quality"));
        constantEmployeeSelectionStrategy.setEmployee(denys);
    }
}
