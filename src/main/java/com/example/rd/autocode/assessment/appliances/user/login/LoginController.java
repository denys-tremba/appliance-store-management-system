package com.example.rd.autocode.assessment.appliances.user.login;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {
    @GetMapping("/login")
    public String login(Authentication user) {
        if (user == null) {
            return "user/login";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("login/ott/username")
    public String getOttForm() {
        return "user/ott";
    }
}
