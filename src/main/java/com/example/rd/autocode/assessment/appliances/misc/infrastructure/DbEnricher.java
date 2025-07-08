package com.example.rd.autocode.assessment.appliances.misc.infrastructure;

import com.example.rd.autocode.assessment.appliances.appliance.ApplianceRepository;
import com.example.rd.autocode.assessment.appliances.appliance.find.FindApplianceService;
import com.example.rd.autocode.assessment.appliances.appliance.manage.ManageApplianceService;
import com.example.rd.autocode.assessment.appliances.misc.infrastructure.ai.AiAssistant;
import com.example.rd.autocode.assessment.appliances.user.admin.manageUsers.Admin;
import com.example.rd.autocode.assessment.appliances.user.client.signUp.Client;
import com.example.rd.autocode.assessment.appliances.user.employee.ConstantEmployeeSelectionStrategy;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.Employee;
import com.example.rd.autocode.assessment.appliances.user.login.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Profile("!prod")
@Slf4j
public class DbEnricher implements CommandLineRunner, BeanPostProcessor {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final ConstantEmployeeSelectionStrategy constantEmployeeSelectionStrategy;
    final VectorStore vectorStore;
    final ApplianceRepository applianceRepository;
    final AiAssistant aiAssistant;
    final FindApplianceService findApplianceService;

    @Override
    public void run(String... args) throws Exception {

        Client charlies = userRepository.save(Client.create("Charlies", "charlies@gmail.com", passwordEncoder.encode("66P[:0NVkWCa"), "4539899931752296"));
        Client bob = userRepository.save(Client.create("Bob", "bob@gmail.com", passwordEncoder.encode("MmX55w1>C0`:"), "4916830689174006"));
        Employee oliver = userRepository.save(Employee.create("Oliver", "oliver@gmail.com", passwordEncoder.encode(">0w.y94Gd6,,"), "Laundry"));
        Employee denys = userRepository.save(Employee.create("Denys", "denys.tremba.trying@gmail.com", passwordEncoder.encode("5V38&62e!Lv^"), "Air Quality"));
        userRepository.save(Admin.create());
        constantEmployeeSelectionStrategy.setEmployee(denys);

        applianceRepository.findAll().forEach(a-> vectorStore.add(Collections.singletonList(ManageApplianceService.convertToDoc(a))));


    }
}
