package com.example.rd.autocode.assessment.appliances.user.employee.signUp;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(properties = "spring.sql.init.data-locations=classpath:static/sql/data.sql")
class EmployeeRepositoryIT {
    @Autowired
    EmployeeRepository employeeRepository;
    @Test
    void testFind() {
        Assertions.assertThat(employeeRepository.findFirstWithMinOrderCount()).isNotEmpty();
    }
}