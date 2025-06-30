package com.example.rd.autocode.assessment.appliances.order;

import com.example.rd.autocode.assessment.appliances.user.client.signUp.Client_;
import com.example.rd.autocode.assessment.appliances.user.employee.signUp.Employee_;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecifications {
    public static Specification<Order> hasState(OrderState state) {
        return (r, q, c) -> c.equal(r.get(Order_.state), state);
    }

    public static Specification<Order> hasClientWithUsername(String username) {
        return (r, q, b) -> b.equal(r.get(Order_.client).get(Client_.email), username);
    }

    public static Specification<Order> hasEmployeeWithUsername(String username) {
        return (r, q, b) -> b.equal(r.get(Order_.employee).get(Employee_.email), username);
    }
}
