package com.example.rd.autocode.assessment.appliances.appliance.find;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.appliance.Appliance_;
import com.example.rd.autocode.assessment.appliances.appliance.Category;
import com.example.rd.autocode.assessment.appliances.appliance.PowerType;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

@Data
public class ApplianceSearchForm {
    PowerType[] powerTypes;
    Category[] categories;
    BigDecimal priceStart;
    BigDecimal priceEnd;

    public Specification<Appliance> toSpecification() {
        Specification<Appliance> specification = conjunction();

        if (powerTypes != null && powerTypes.length != 0) {
            specification = specification.and(hasPowerTypeIn(Arrays.asList(powerTypes)));
        }

        if (categories != null && categories.length != 0) {
            specification = specification.and(hasCategoryTypeIn(Arrays.asList(categories)));
        }

        if (priceStart != null || priceEnd != null) {
            priceStart = priceStart == null? BigDecimal.ZERO :priceStart;
            priceEnd = priceEnd == null? BigDecimal.valueOf(Long.MAX_VALUE) :priceEnd;
            specification = specification.and(hasPriceInRange(priceStart, priceEnd));
        }

        return specification;
    }

    static Specification<Appliance> hasPriceInRange(BigDecimal start, BigDecimal end) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(Appliance_.PRICE), start, end);
    }

    static Specification<Appliance> hasPowerTypeIn(Collection<PowerType> powerTypes) {
        return (root, query, criteriaBuilder) -> root.get(Appliance_.POWER_TYPE).in(powerTypes);
    }

    static Specification<Appliance> hasCategoryTypeIn(Collection<Category> categories) {
        return (root, query, criteriaBuilder) -> root.get(Appliance_.CATEGORY).in(categories);
    }

    static Specification<Appliance> conjunction() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }
}
