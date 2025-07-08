package com.example.rd.autocode.assessment.appliances.appliance.manageAppliance;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.appliance.find.ApplianceNotFound;
import com.example.rd.autocode.assessment.appliances.appliance.manage.CreateApplianceForm;
import com.example.rd.autocode.assessment.appliances.appliance.manage.ManageApplianceController;
import com.example.rd.autocode.assessment.appliances.appliance.manage.ManageApplianceService;
import com.example.rd.autocode.assessment.appliances.auxiliary.Appliances;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.ManufacturerRepository;
import com.example.rd.autocode.assessment.appliances.misc.infrastructure.web.EditMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static org.assertj.core.api.HamcrestCondition.matching;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = ManageApplianceController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.example\\.rd\\.autocode\\.assessment\\.appliances\\.misc\\.infrastructure\\..*")})

@WithMockUser(roles = "EMPLOYEE")
class ManageApplianceControllerTest {
    @Autowired
    MockMvcTester mvcTester;
    @MockitoBean
    ManageApplianceService manageApplianceService;
    @MockitoBean
    ManufacturerRepository manufacturerRepository;

    Appliance appliance = Appliances.wellFormed();


    @BeforeEach
    void setUp() {
        when(manufacturerRepository.findById(appliance.getManufacturer().getId())).thenReturn(Optional.ofNullable(appliance.getManufacturer()));
    }

    @Test
    void getCreationForm() {
        mvcTester.get()
                .uri("/appliances/create")
                .exchange()
                .assertThat()
                .hasViewName("appliance/newAppliance")
                .hasStatusOk()
                .model()
                .hasEntrySatisfying("appliance", matching(instanceOf(CreateApplianceForm.class)))
                .hasEntrySatisfying("editMode", matching(is(EditMode.CREATE)));
    }

    @Test
    void getEditForm() {
        when(manageApplianceService.findById(eq(1L))).thenReturn(appliance);

        mvcTester.get()
                .uri("/appliances/{id}/edit", 1L)
                .exchange()
                .assertThat()
                .hasStatusOk()
                .model()
                .hasEntrySatisfying("appliance", matching(instanceOf(CreateApplianceForm.class)))
                .hasEntrySatisfying("editMode", matching(is(EditMode.UPDATE)));
    }

    @Test
    void create() {
    }

    @Nested
    class edit {
        @Test
        void happyPath() {
            when(manageApplianceService.edit(not(eq(1L)), any())).thenThrow(ApplianceNotFound.class);

            mvcTester.post()
                    .uri("/appliances/{id}/edit", 1L)
                    .with(csrf())
                    .params(validParams())
                    .exchange()
                    .assertThat()
                    .hasStatus3xxRedirection()
                    .flash()
                    .hasKeySatisfying(matching(is("message")));
        }

        @Test
        void applianceIsMissing() {
            when(manageApplianceService.edit(not(eq(appliance.getId())), any())).thenThrow(ApplianceNotFound.class);

            mvcTester.post()
                    .uri("/appliances/{id}/edit", appliance.getId() + 1L)
                    .with(csrf())
                    .params(validParams())
                    .exchange()
                    .assertThat()
                    .failure();
        }


        @Test
        void validationViolated() {
            mvcTester.post()
                    .uri("/appliances/{id}/edit", 1L)
                    .with(csrf())
                    .exchange()
                    .assertThat()
                    .hasStatus2xxSuccessful()
                    .hasViewName("appliance/newAppliance")
                    .model()
                    .hasErrors();
        }
    }


    MultiValueMap<String, String> validParams() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                map.addIfAbsent("id", String.valueOf(appliance.getId()));
                map.addIfAbsent("name", appliance.getName());
                map.addIfAbsent("category", appliance.getCategory().name());
                map.addIfAbsent("model", appliance.getModel());
                map.addIfAbsent("powerType", appliance.getPowerType().name());
                map.addIfAbsent("manufacturer", String.valueOf(appliance.getManufacturer().getId()));
                map.addIfAbsent("characteristic", appliance.getCharacteristic());
                map.addIfAbsent("description", appliance.getDescription());
                map.addIfAbsent("power", String.valueOf(appliance.getPower()));
                map.addIfAbsent("price", String.valueOf(appliance.getPrice()));
        return map;
    }

    @Test
    void delete() {
        mvcTester.post()
                .uri("/appliances/{id}/delete", 1L)
                .with(csrf())
                .exchange()
                .assertThat()
                .hasStatus3xxRedirection()
                .hasRedirectedUrl("/appliances")
                .flash()
                .hasKeySatisfying(matching(is("message")));
    }
}