package com.example.rd.autocode.assessment.appliances.misc.infrastructure.ai;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.appliance.ApplianceRepository;
import com.example.rd.autocode.assessment.appliances.appliance.Category;
import com.example.rd.autocode.assessment.appliances.appliance.find.FindApplianceService;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.Manufacturer;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.ManufacturerRepository;
import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.order.complete.CompleteOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.SessionStatus;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AiTools {
    private final ApplianceRepository applianceRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final FindApplianceService findApplianceService;

    @Tool(description = "Create manufacturer")
    public void createManufacturer(String name) {
        manufacturerRepository.save(Manufacturer.create(name));
    }
//    @Tool(description = "Enter line item into CLIENT order only if he gives confirmation")
//    public void enterLineItem(@ToolParam(description = "Appliance identifier") Long id, ToolContext toolContext) {
//        CompleteOrderService orderService = (CompleteOrderService) toolContext.getContext().get("orderService");
//        orderService.enterLineItem(id, 1L);
//    }
    @Tool(description = "Enter appliance into order")
    public void enterLineItem(@ToolParam(description = "client preferences about appliance") String preferences,
                              @ToolParam(description = "appliance category")  Category category,
                              @ToolParam(description = "client budget") BigDecimal budget,
                              ToolContext toolContext) {
        Appliance appliances = findApplianceService.semanticSearchAllByDescriptionAndCategory(preferences, category, budget).getContent().get(0);
        CompleteOrderService orderService = (CompleteOrderService) toolContext.getContext().get("orderService");
        orderService.enterLineItem(appliances.getId(), 1L);
    }
    @Tool(description = "Find appliances by client preferences, category and budget")
    public List<Appliance> findAppliances(@ToolParam(description = "client preferences about appliance") String preferences,
                                          @ToolParam(description = "appliance category")  Category category,
                                          @ToolParam(description = "client budget") BigDecimal budget,
                                          ToolContext toolContext) {
        return findApplianceService.semanticSearchAllByDescriptionAndCategory(preferences, category, budget).getContent();
    }
    @Tool(description = "List categories")
    public Set<Category> listCategories() {
        return EnumSet.allOf(Category.class);
    }
    @Tool(description = "Fetch order details")
    public Order getLineItems(ToolContext toolContext) {
        CompleteOrderService orderService = (CompleteOrderService) toolContext.getContext().get("orderService");
        return orderService.getOrder();
    }
    @Tool(description = "Remove appliance from order")
    public void removeLineItem(@ToolParam(description = "Appliance identifier") Long id, ToolContext toolContext) {
        CompleteOrderService orderService = (CompleteOrderService) toolContext.getContext().get("orderService");
        orderService.removeOrderLineItemsWithApplianceId(id);
    }
    @Tool(description = "Complete order")
    public void completeOrder(ToolContext toolContext) {
        CompleteOrderService orderService = (CompleteOrderService) toolContext.getContext().get("orderService");
        SessionStatus sessionStatus = (SessionStatus) toolContext.getContext().get("sessionStatus");
        orderService.completeOrder();
        sessionStatus.setComplete();
    }
}
