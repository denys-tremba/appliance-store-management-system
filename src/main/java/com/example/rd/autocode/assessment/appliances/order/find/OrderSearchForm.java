package com.example.rd.autocode.assessment.appliances.order.find;

import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.order.OrderSpecifications;
import com.example.rd.autocode.assessment.appliances.order.OrderState;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
@Data
public abstract class OrderSearchForm {
    Optional<OrderState> state;
    String email;

    public Specification<Order> toSpecification() {
        Specification<Order> specification = conjunction();

        if (state != null && state.isPresent()) {
            specification = specification.and(OrderSpecifications.hasState(state.get()));
        }
        return specification.and(getSpecificationForUser());
    }

    protected abstract Specification<Order> getSpecificationForUser();

    static Specification<Order> conjunction() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }
}
