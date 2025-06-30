package com.example.rd.autocode.assessment.appliances.user.employee.signUp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = """
            SELECT u.*, COUNT(o.id) as order_count
            FROM users u
            LEFT JOIN orders o ON u.id = o.employee_id
            WHERE u.role = 'employee'
            GROUP BY u.id
            ORDER BY COUNT(o.id) ASC, u.id ASC
            LIMIT 1
            """, nativeQuery = true)
    Optional<Employee> findFirstWithMinOrderCount();
}
