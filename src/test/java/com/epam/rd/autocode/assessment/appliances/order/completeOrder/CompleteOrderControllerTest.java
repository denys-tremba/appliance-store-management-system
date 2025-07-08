//package com.example.rd.autocode.assessment.appliances.order.completeOrder;
//
//import com.example.rd.autocode.assessment.appliances.auxiliary.OrdersBuilder;
//import com.example.rd.autocode.assessment.appliances.order.Order;
//import com.example.rd.autocode.assessment.appliances.order.complete.CompleteOrderController;
//import com.example.rd.autocode.assessment.appliances.order.complete.CompleteOrderService;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.assertj.MockMvcTester;
//
//import static org.assertj.core.api.HamcrestCondition.matching;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.AdditionalMatchers.not;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//
//@WebMvcTest(controllers = CompleteOrderController.class)
//@WithMockUser(roles = "CLIENT")
//class CompleteOrderControllerTest {
//    @Autowired
//    MockMvcTester mvcTester;
//
//    @MockitoBean
//    CompleteOrderService completeOrderService;
//    Order order = new OrdersBuilder().build();
//
//    @Test
//    void getCurrentOrder() {
//        when(completeOrderService.getOgetCurrentOrder()).thenReturn(order);
//
//        mvcTester.get()
//                .uri("/orders/current")
//                .sessionAttr("orderService", completeOrderService)
//                .exchange()
//                .assertThat()
//                .hasViewName("order/editOrder")
//                .hasStatusOk()
//                .model()
//                .hasEntrySatisfying("order", matching(is(order)));
//    }
//
//    @Test
//    void clearOrder() {
//        mvcTester.post()
//                .uri("/orders/current/delete")
//                .with(csrf())
//                .sessionAttr("orderService", completeOrderService)
//                .exchange()
//                .assertThat()
//                .hasStatus3xxRedirection()
//                .hasRedirectedUrl("/orders/current")
//                .flash()
//                .hasEntrySatisfying("message", matching(Matchers.containsString("cleared")));
//    }
//
//    @Test
//    void removeLineItem() {
//        mvcTester.post()
//                .uri("/orders/current/lineItem/{id}/remove", 0L)
//                .with(csrf())
//                .sessionAttr("orderService", completeOrderService)
//                .exchange()
//                .assertThat()
//                .hasStatus3xxRedirection()
//                .hasRedirectedUrl("/orders/current")
//                .flash()
//                .hasEntrySatisfying("message", matching(Matchers.containsString("deleted")));
//    }
//
//    @Test
//    void completeOrder() {
//        mvcTester.post()
//                .uri("/orders/current/complete")
//                .with(csrf())
//                .sessionAttr("orderService", completeOrderService)
//                .exchange()
//                .assertThat()
//                .hasStatus3xxRedirection()
//                .hasRedirectedUrl("/orders/current")
//                .flash()
//                .hasEntrySatisfying("message", matching(Matchers.containsString("completed")));
//    }
//
//    @Test
//    void enterLineItem() {
//        mvcTester.post()
//                .uri("/orders/current/lineItem/enter")
//                .with(csrf())
//                .sessionAttr("orderService", completeOrderService)
//                .exchange()
//                .assertThat()
//                .hasStatus3xxRedirection()
//                .hasRedirectedUrl("/orders/current")
//                .flash()
//                .hasEntrySatisfying("message", matching(Matchers.containsString("entered")));
//    }
//
//    @Test
//    void editLineItem() {
//        doThrow(RuntimeException.class)
//                .when(completeOrderService)
//                .updateLineItemQuantity(not(eq(0)), not(eq(2L)), );
//
//        mvcTester.post()
//                .uri("/orders/current/lineItem/{id}/edit", 0)
//                .with(csrf())
//                .param("quantity", String.valueOf(2L))
//                .sessionAttr("orderService", completeOrderService)
//                .exchange()
//                .assertThat()
//                .hasStatus3xxRedirection()
//                .hasRedirectedUrl("/orders/current")
//                .flash()
//                .hasEntrySatisfying("message", matching(Matchers.containsString("edited")));
//    }
//}