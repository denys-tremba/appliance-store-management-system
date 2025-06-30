package com.example.rd.autocode.assessment.appliances.order.find;

import com.example.rd.autocode.assessment.appliances.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/charts")
@RequiredArgsConstructor
public class FindChartsController {
    private final OrderRepository orderRepository;
    @GetMapping
    public String getCharts(Model model) {
        model.addAttribute("orderSummaries", orderRepository.findAllOrderSummaries());
        return "order/statistics";
    }
}
