package com.example.rd.autocode.assessment.appliances.appliance.findAppliance;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.appliance.ApplianceRepository;
import com.example.rd.autocode.assessment.appliances.appliance.find.FindApplianceController;
import com.example.rd.autocode.assessment.appliances.appliance.find.FindApplianceService;
import com.example.rd.autocode.assessment.appliances.auxiliary.ApplianceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FindApplianceController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.example\\.rd\\.autocode\\.assessment\\.appliances\\.misc\\.infrastructure\\..*")})
@WithMockUser
class FindApplianceControllerTest {
    @TestConfiguration
    static class TestConfig {
        @Bean
        UriComponentsBuilder uriComponentsBuilder() {
            return UriComponentsBuilder.newInstance();
        }
    }
    @Autowired
    MockMvcTester mvcTester;
    @MockitoBean
    FindApplianceService findApplianceService;
    @MockitoBean
    ApplianceRepository applianceRepository;
    @MockitoBean
    VectorStore vectorStore;
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
        when(applianceRepository.findAll(ArgumentMatchers.<Specification<Appliance>>any(), ArgumentMatchers.<Pageable>any()))
                .thenReturn(page);

        mvcTester.get()
                .uri("/appliances")
                .param("size", String.valueOf(page.getSize()))
                .exchange()
                .assertThat()
                .failure()
//                .model()
//                .hasKeySatisfying(matching(containsString("appliances")))
                ;
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
                .failure()
//                .model()
//                .hasKeySatisfying(matching(is("appliances")))
                ;
    }
}