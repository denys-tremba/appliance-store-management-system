package com.example.rd.autocode.assessment.appliances.user.manageAccount;

import com.example.rd.autocode.assessment.appliances.user.login.DefaultUserDetailsManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = ManageAccountController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.example\\.rd\\.autocode\\.assessment\\.appliances\\.misc\\.infrastructure\\..*")})
@WithMockUser
class ManageAccountControllerTest {


    @Autowired
    MockMvcTester mvcTester;
    @MockitoBean
    DefaultUserDetailsManager defaultUserDetailsManager;

    @Test
    void getChangePasswordForm() {
        mvcTester.get().uri("/manageAccount/changePassword").exchange().assertThat().hasStatusOk();

    }

    @Test
    void changePassword() {
        mvcTester.post().with(csrf()).uri("/manageAccount/changePassword").param("password", "n2UixNeZWiLtbVP").param("passwordRepeated", "n2UixNeZWiLtbVP").exchange().assertThat().hasStatus3xxRedirection();
    }

    @Test
    void deleteAccount() {
        mvcTester.post().with(csrf()).uri("/manageAccount/delete").exchange().assertThat().hasStatus3xxRedirection();
    }
}