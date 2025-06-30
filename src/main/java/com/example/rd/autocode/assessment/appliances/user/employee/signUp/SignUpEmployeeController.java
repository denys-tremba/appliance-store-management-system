package com.example.rd.autocode.assessment.appliances.user.employee.signUp;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation.ValidationGroupSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employees")
public class SignUpEmployeeController {
    private final EmployeeRepository employeeRepository;
    private final SignUpEmployeeService signUpEmployeeService;
    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "employee/employees";
    }
    @GetMapping("/signUp")
    public String getCreationForm(Model model) {
        model.addAttribute("employee", new SignUpEmployeeForm());
        return "employee/newEmployee";
    }

    @PostMapping("/signUp")
    public String doSignUp(@Validated(ValidationGroupSequence.class) @ModelAttribute("employee") SignUpEmployeeForm form,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "employee/newEmployee";
        }
        signUpEmployeeService.save(form.getName(), form.getEmail(), form.getPassword(), form.getDepartment());
        redirectAttributes.addFlashAttribute("message", "You are successfully registered");
        return "redirect:/login";
    }
}
