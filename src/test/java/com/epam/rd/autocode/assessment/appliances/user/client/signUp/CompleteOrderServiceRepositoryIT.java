package com.example.rd.autocode.assessment.appliances.user.client.signUp;

import com.example.rd.autocode.assessment.appliances.order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = "spring.sql.init.data-locations=classpath:static/sql/data.sql")
class CompleteOrderServiceRepositoryIT {

    @Autowired
    OrderRepository repository;
    @Test
    void findPendingOrder() {
        List order = repository.findPendingOrders("alice.johnson@example.com");
        assertThat(order).hasSize(1);
        order = repository.findPendingOrders("david.miller@example.com");
        assertThat(order).hasSize(1);
        order = repository.findPendingOrders("bob.williams@example.com");
        assertThat(order).hasSize(0);
    }
}