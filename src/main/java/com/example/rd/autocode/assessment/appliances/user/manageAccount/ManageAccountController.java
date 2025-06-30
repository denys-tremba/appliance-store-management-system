package com.example.rd.autocode.assessment.appliances.user.manageAccount;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation.ValidationGroupSequence;
import com.example.rd.autocode.assessment.appliances.user.login.DefaultUserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manageAccount")
public class ManageAccountController {
    private final DefaultUserDetailsManager defaultUserDetailsManager;
    @GetMapping(value = "/changePassword")
    public String getChangePasswordForm(@ModelAttribute("form") EnterPasswordForm form) {
        return "user/changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@Validated(ValidationGroupSequence.class) @ModelAttribute("form") EnterPasswordForm form, BindingResult bindingResult, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/changePassword";
        }
        defaultUserDetailsManager.changePassword(user, form.getPassword());
        redirectAttributes.addFlashAttribute("message", "Your password is successfully changed");
        return "redirect:/";
    }
    @PostMapping("/delete")
    public String deleteAccount(@AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        defaultUserDetailsManager.deleteUser(user.getUsername());
        redirectAttributes.addFlashAttribute("message", "Your account is successfully deleted");
        return "redirect:/logout";
    }
}
