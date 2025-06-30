package com.example.rd.autocode.assessment.appliances.appliance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ApplianceRepository extends JpaRepository<Appliance, Long>, PagingAndSortingRepository<Appliance, Long>, JpaSpecificationExecutor<Appliance> {
    Page<Appliance> findByDescriptionContaining(String text, Pageable pageable);

    Page<Appliance> findByCategory(Category category, Pageable pageable);
}
