package com.example.rd.autocode.assessment.appliances.order.approveOrder;

import com.example.rd.autocode.assessment.appliances.order.approve.ApproveOrderController;
import com.example.rd.autocode.assessment.appliances.order.approve.ApproveOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.HamcrestCondition.matching;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = ApproveOrderController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.example\\.rd\\.autocode\\.assessment\\.appliances\\.misc\\.infrastructure\\..*")})

@WithMockUser(roles = "EMPLOYEE")
class ApproveCompleteOrderControllerTestService {
    @Autowired
    MockMvcTester mvcTester;
    @MockitoBean
    ApproveOrderService approveOrderService;

    @Test
    void approve() {
        mvcTester.post()
                .with(csrf())
                .uri("/orders/{id}/approve", 1L)
                .exchange()
                .assertThat()
                .hasStatus3xxRedirection()
                .hasRedirectedUrl("/orders/employee")
                .flash()
                .hasKeySatisfying(matching(is("message")));
    }
}