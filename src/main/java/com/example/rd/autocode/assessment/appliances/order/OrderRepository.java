package com.example.rd.autocode.assessment.appliances.order;

import com.example.rd.autocode.assessment.appliances.order.find.OrderSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface OrderRepository extends JpaRepository<Order, Long>, PagingAndSortingRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @NativeQuery("""
            select o.*
            from orders as o
            join users as u
            on o.client_id = u.id
            where u.email = :email
            and o.state = 'WAITING_FOR_APPROVAL'
            """)
    List<Order> findPendingOrders(@Param("email") String username);

    @Query("select new com.example.rd.autocode.assessment.appliances.order.find.OrderSummary(o.employee.name, count(o)) " +
           "from Order o " +
           "where o.employee is not null " +
           "group by o.employee.name")
    List<OrderSummary> findAllOrderSummaries();

    @Query(value = "select exists (" +
           " select 1" +
           " from order_line_items items "+
           " where items.appliance_id = :appliance_id)", nativeQuery = true)
    boolean checkApplianceUsageInLineItems(@Param("appliance_id") Long applianceId);

    Page<Order> findAllByClientEmail(Pageable pageable, String name);

    Page<Order> findAllByEmployeeEmail(Pageable pageable, String name);
}

