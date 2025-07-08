package com.example.rd.autocode.assessment.appliances.misc.infrastructure.ai;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.appliance.Category;
import com.example.rd.autocode.assessment.appliances.appliance.find.FindApplianceService;
import com.example.rd.autocode.assessment.appliances.order.Order;
import com.example.rd.autocode.assessment.appliances.order.complete.CompleteOrderSessionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AiTools {
    private final FindApplianceService findApplianceService;

    @Tool(description = "Enter appliance into order")
    public void enterLineItem(@ToolParam(description = "Appliance identifier") Long id,
                              ToolContext toolContext) {
        CompleteOrderSessionHandler sessionHandler = (CompleteOrderSessionHandler) toolContext.getContext().get("sessionHandler");
        sessionHandler.enterLineItem(id, 1L);
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
    @Tool(description = "Fetch current order")
    public Order getLineItems(ToolContext toolContext) {
        CompleteOrderSessionHandler sessionHandler = (CompleteOrderSessionHandler) toolContext.getContext().get("sessionHandler");
        return sessionHandler.getOrder();
    }
    @Tool(description = "Remove appliance from order")
    public void removeLineItem(@ToolParam(description = "Appliance identifier") Long id, ToolContext toolContext) {
        CompleteOrderSessionHandler sessionHandler = (CompleteOrderSessionHandler) toolContext.getContext().get("sessionHandler");
        sessionHandler.removeOrderLineItemsWithApplianceId(id);
    }
    @Tool(description = "Complete order")
    public void completeOrder(ToolContext toolContext) {
        CompleteOrderSessionHandler sessionHandler = (CompleteOrderSessionHandler) toolContext.getContext().get("sessionHandler");
        sessionHandler.completeOrder();
    }
}
