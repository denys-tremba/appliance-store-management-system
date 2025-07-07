package com.example.rd.autocode.assessment.appliances.order.complete;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders/current")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SessionAttributes("orderService")
@PreAuthorize("hasRole('CLIENT')")
public class CompleteOrderController {
    ApplicationContext context;


    @GetMapping
    public String getCurrentOrder(@ModelAttribute("orderService") CompleteOrderService completeOrderService, Model model) {
        model.addAttribute("order", completeOrderService.getCurrentOrder());
        return "order/editOrder";
    }

    @PostMapping("/delete")
    public String clearOrder(RedirectAttributes redirectAttributes,
                             @ModelAttribute("orderService") CompleteOrderService completeOrderService,
                             SessionStatus sessionStatus) {
        redirectAttributes.addFlashAttribute("message", "Order is successfully cleared");
        completeOrderService.clearOrder();
        sessionStatus.setComplete();
        return "redirect:/orders/current";
    }

    @PostMapping("/lineItem/{id}/remove")
    public String removeLineItem(@PathVariable("id") int id, RedirectAttributes redirectAttributes, @ModelAttribute("orderService") CompleteOrderService completeOrderService) {
        redirectAttributes.addFlashAttribute("message", "Line item is successfully deleted");
        completeOrderService.removeOrderLineItemAt(id);
        return "redirect:/orders/current";
    }

    @PostMapping("/complete")
    public String completeOrder(RedirectAttributes redirectAttributes, SessionStatus sessionStatus, @ModelAttribute("orderService") CompleteOrderService completeOrderService) {
        redirectAttributes.addFlashAttribute("message", "Order is completed");
        completeOrderService.completeOrder();
        sessionStatus.setComplete();
        return "redirect:/orders/client";
    }

    @PostMapping("/lineItem/enter")
    public String enterLineItem(EnterLineItemForm form,
                                RedirectAttributes redirectAttributes,
                                @ModelAttribute("orderService") CompleteOrderService completeOrderService) {
        completeOrderService.enterLineItem(form.getApplianceId(), form.getNumbers());
        redirectAttributes.addFlashAttribute("message", "Appliance " + form.getApplianceId() + " is entered");
        return "redirect:/orders/current";
    }
    @PostMapping("/{id}/revoke")
    public String revoke(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, @ModelAttribute("orderService") CompleteOrderService completeOrderService) {
        completeOrderService.revoke(id);
        redirectAttributes.addFlashAttribute("message", "Order is revoked");
        return "redirect:/orders/client";
    }
    @PostMapping("/lineItem/{id}/edit")
    public String updateLineItemQuantity(@PathVariable("id") int id,
                                         EditLineItemForm form,
                                         RedirectAttributes redirectAttributes,
                                         @ModelAttribute("orderService") CompleteOrderService completeOrderService) {
        completeOrderService.updateLineItemQuantity(id, form.getQuantity());
        redirectAttributes.addFlashAttribute("message", "Line item is edited");
        return "redirect:/orders/current";
    }

    @ModelAttribute(name = "orderService")
    CompleteOrderService completeOrderService(@AuthenticationPrincipal User user) {
        CompleteOrderService completeOrderService = context.getBean(CompleteOrderService.class);
        return completeOrderService;
    }
}
