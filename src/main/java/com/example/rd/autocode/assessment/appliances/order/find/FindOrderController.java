package com.example.rd.autocode.assessment.appliances.order.find;

import com.example.rd.autocode.assessment.appliances.order.*;
import com.example.rd.autocode.assessment.appliances.order.approve.OrderMapper;
import com.example.rd.autocode.assessment.appliances.order.approve.OrderRecord;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FindOrderController {
    OrderRepository orderRepository;

    @GetMapping("/client")
    @PreAuthorize("hasRole('CLIENT')")
    public String findAllForClient(@SortDefault.SortDefaults({@SortDefault("state"), @SortDefault("createdAt"), @SortDefault("id")}) Pageable pageable,
                          Model model,
                          Authentication authentication) {
        Page<Order> page = orderRepository.findAllByClientEmail(pageable, authentication.getName());
        model.addAttribute("orders", recreateOrdersPage(pageable, page));
        return "order/orders";
    }

    @GetMapping("/employee")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String findAllForEmployee(@SortDefault.SortDefaults({@SortDefault("state"), @SortDefault("createdAt"), @SortDefault("id")}) Pageable pageable,
                                   Model model,
                                   Authentication authentication) {
        Page<Order> page = orderRepository.findAllByEmployeeEmail(pageable, authentication.getName());
        model.addAttribute("orders", recreateOrdersPage(pageable, page));
        return "order/orders";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String findAll(@SortDefault.SortDefaults({@SortDefault("state"), @SortDefault("createdAt"), @SortDefault("id")}) Pageable pageable,
                          Model model) {
        Page<Order> page = orderRepository.findAll(pageable);
        model.addAttribute("orders", recreateOrdersPage(pageable, page));
        return "order/orders";
    }

    @GetMapping("/{id}")
    public String peek(@PathVariable("id") Long id, Model model) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFound::new);
        model.addAttribute("order", order);
        model.addAttribute("readOnly", true);
        return "order/editOrder";
    }

    @GetMapping(path = "/admin", params = "criteria")
    public String findAllByCriteriaForAdmin(@SortDefault.SortDefaults({@SortDefault("state"), @SortDefault("createdAt"), @SortDefault("id")}) Pageable pageable,
                                            Model model, AdminOrderSearchForm form, @AuthenticationPrincipal User user) {
        return doFindAllByCriteria(pageable, model, form, "admin");
    }

    @GetMapping(path = "/client", params = "criteria")
    public String findAllByCriteriaForClient(@SortDefault.SortDefaults({@SortDefault("state"), @SortDefault("createdAt"), @SortDefault("id")}) Pageable pageable,
                                             Model model, ClientOrderSearchForm form, @AuthenticationPrincipal User user) {
        return doFindAllByCriteria(pageable, model, form, user.getUsername());
    }

    @GetMapping(path = "/employee", params = "criteria")
    public String findAllByCriteriaForEmployee(@SortDefault.SortDefaults({@SortDefault("state"), @SortDefault("createdAt"), @SortDefault("id")}) Pageable pageable,
                                               Model model, EmployeeOrderSearchForm form, @AuthenticationPrincipal User user) {
        return doFindAllByCriteria(pageable, model, form, user.getUsername());
    }

    private String doFindAllByCriteria(Pageable pageable, Model model, OrderSearchForm form, String email) {
        form.setEmail(email);
        Specification<Order> specification = form.toSpecification();
        Page<Order> page = orderRepository.findAll(specification, pageable);
        model.addAttribute("orders", recreateOrdersPage(pageable, page));
        return "order/orders";
    }

    private Page<OrderRecord> recreateOrdersPage(Pageable pageable, Page<Order> page) {
        List<OrderRecord> dtos = OrderMapper.INSTANCE.toDto(page.getContent());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @ModelAttribute
    public void populateModel(Model model) {
        model.addAttribute("sorts", OrderSort.values());
        model.addAttribute("states", OrderState.values());
    }
}
