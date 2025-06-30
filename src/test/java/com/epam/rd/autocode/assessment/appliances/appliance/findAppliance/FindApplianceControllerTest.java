package com.example.rd.autocode.assessment.appliances.appliance.findAppliance;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.appliance.ApplianceRepository;
import com.example.rd.autocode.assessment.appliances.appliance.find.FindApplianceController;
import com.example.rd.autocode.assessment.appliances.auxiliary.ApplianceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;

import static org.assertj.core.api.HamcrestCondition.matching;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FindApplianceController.class)
@WithMockUser
class FindApplianceControllerTest {
    @Autowired
    MockMvcTester mvcTester;
    @MockitoBean
    ApplianceRepository applianceRepository;
    Page<Appliance> page;

    @BeforeEach
    void setUp() {
        Appliance dishwasher = new ApplianceBuilder().name("dishwasher").build();
        Appliance microwave = new ApplianceBuilder().name("microwave oven").build();
        List<Appliance> content = List.of(dishwasher, microwave);
        page = new PageImpl<>(content, PageRequest.ofSize(content.size()), content.size());
    }

    @Test
    void findAllBySpecification() {
        when(applianceRepository.findAll(ArgumentMatchers.<Specification<Appliance>>any(), argThat((ArgumentMatcher<Pageable>) argument -> argument.getPageSize() == page.getSize())))
                .thenReturn(page);

        mvcTester.get()
                .uri("/appliances")
                .param("size", String.valueOf(page.getSize()))
                .exchange()
                .assertThat()
                .model()
                .hasKeySatisfying(matching(containsString("appliances")));
    }

    @Test
    void findAllByDescriptionContent() {
        when(applianceRepository.findByDescriptionContaining(eq("washer"), eq(page.getPageable())))
                .thenReturn(page);

        mvcTester.get()
                .uri("/appliances/search")
                .param("text", "washer")
                .param("size", String.valueOf(page.getSize()))
                .exchange()
                .assertThat()
                .model()
                .hasKeySatisfying(matching(is("appliances")));
    }
}