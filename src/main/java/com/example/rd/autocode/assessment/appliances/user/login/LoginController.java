package com.example.rd.autocode.assessment.appliances.user.login;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String login(Authentication user) {
        if (user == null) {
            return "user/login";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/ott/username")
    public String getOttForm() {
        return "user/ott";
    }
}
