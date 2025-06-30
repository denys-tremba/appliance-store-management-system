package com.example.rd.autocode.assessment.appliances.order.find;

import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.order.OrderSpecifications;
import org.springframework.data.jpa.domain.Specification;

public class ClientOrderSearchForm extends OrderSearchForm {
    protected Specification<Order> getSpecificationForUser() {
        return OrderSpecifications.hasClientWithUsername(email);
    }
}
