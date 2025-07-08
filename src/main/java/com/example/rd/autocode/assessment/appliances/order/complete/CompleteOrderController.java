package com.example.rd.autocode.assessment.appliances.order.complete;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders/current")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('CLIENT')")
public class CompleteOrderController {


    @GetMapping
    public String getCurrentOrder(@RequestAttribute("sessionHandler") CompleteOrderSessionHandler handler, Model model) {
        model.addAttribute("order", handler.getOrder());
        return "order/editOrder";
    }

    @PostMapping("/delete")
    public String clearOrder(RedirectAttributes redirectAttributes, @RequestAttribute("sessionHandler") CompleteOrderSessionHandler handler) {
        redirectAttributes.addFlashAttribute("message", "Order is successfully cleared");
        handler.clearOrder();
        return "redirect:/orders/current";
    }

    @PostMapping("/lineItem/{id}/remove")
    public String removeLineItem(@PathVariable("id") int id, RedirectAttributes redirectAttributes, @RequestAttribute("sessionHandler") CompleteOrderSessionHandler handler) {
        handler.removeLineItemAt(id);
        redirectAttributes.addFlashAttribute("message", "Line item is successfully deleted");
        return "redirect:/orders/current";
    }

    @PostMapping("/complete")
    public String completeOrder(RedirectAttributes redirectAttributes, @RequestAttribute("sessionHandler") CompleteOrderSessionHandler handler) {
        handler.completeOrder();
        redirectAttributes.addFlashAttribute("message", "Order is completed");
        return "redirect:/orders/current";
    }

    @PostMapping("/lineItem/enter")
    public String enterLineItem(EnterLineItemForm form,
                                RedirectAttributes redirectAttributes,
                                @RequestAttribute("sessionHandler") CompleteOrderSessionHandler handler) {
        handler.enterLineItem(form.getApplianceId(), form.getNumbers());
        redirectAttributes.addFlashAttribute("message", "Appliance " + form.getApplianceId() + " is entered");
        return "redirect:/orders/current";
    }
    @PostMapping("/{id}/revoke")
    public String revoke(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, @ModelAttribute("orderService") CompleteOrderService completeOrderService) {
//        completeOrderService.revoke(id);
        redirectAttributes.addFlashAttribute("message", "Order is revoked");
        return "redirect:/orders/client";
    }
    @PostMapping("/lineItem/{id}/edit")
    public String updateLineItemQuantity(@PathVariable("id") int id,
                                         EditLineItemForm form,
                                         RedirectAttributes redirectAttributes,
                                         @RequestAttribute("sessionHandler") CompleteOrderSessionHandler handler) {
        handler.updateLineItemQuantity(id, form.getQuantity());
        redirectAttributes.addFlashAttribute("message", "Line item is edited");
        return "redirect:/orders/current";
    }

}
