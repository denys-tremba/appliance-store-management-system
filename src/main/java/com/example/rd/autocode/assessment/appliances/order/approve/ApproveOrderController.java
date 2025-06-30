package com.example.rd.autocode.assessment.appliances.order.approve;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('EMPLOYEE')")
public class ApproveOrderController {
    ApproveOrderService approveOrderService;

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        approveOrderService.approve(id);
        redirectAttributes.addFlashAttribute("message", "Order " + id + " is approved");
        return "redirect:/orders/employee";
    }

    @PostMapping("/{id}/disapprove")
    public String disapprove(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        approveOrderService.disapprove(id);
        redirectAttributes.addFlashAttribute("message", "Order " + id + " is unapproved");
        return "redirect:/orders/employee";
    }
}
