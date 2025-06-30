package com.example.rd.autocode.assessment.appliances.order.find;

import com.example.rd.autocode.assessment.appliances.order.Order;
import org.springframework.data.jpa.domain.Specification;

public class AdminOrderSearchForm extends OrderSearchForm {
    protected Specification<Order> getSpecificationForUser() {
        return null;
    }
}
