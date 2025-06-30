package com.example.rd.autocode.assessment.appliances.user.admin.manageUsers;

import com.example.rd.autocode.assessment.appliances.user.UserMapper;
import com.example.rd.autocode.assessment.appliances.user.login.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
@Transactional
public class ManageUsersController {
    private final UserRepository userRepository;

    @GetMapping
    public String all(Model model) {
        model.addAttribute("users", UserMapper.INSTANCE.toDto(userRepository.findAll()));
        return "user/users";
    }

    @PostMapping("/{id}/lock")
    public String lock(@PathVariable("id") Long id) {
        com.example.rd.autocode.assessment.appliances.user.User user = userRepository.findById(id).orElseThrow();
        user.setLocked(true);
        return "redirect:/users";
    }
}
