package com.example.rd.autocode.assessment.appliances.user.client.signUp;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation.ValidationGroupSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
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
@RequestMapping("/clients")
public class SignUpClientController {
    private final ClientRepository clientRepository;
    private final SignUpClientService signUpClientService;

    @GetMapping
    public String getAll(Model model,
                         @SortDefault.SortDefaults({@SortDefault("name"), @SortDefault("id")}) Pageable pageable) {
        model.addAttribute("clients", clientRepository.findAll(pageable));
        return "client/clients";
    }

    @GetMapping("/signUp")
    public String getCreationForm(Model model) {
        model.addAttribute("client", new SignUpClientForm());
        return "client/newClient";
    }

    @PostMapping("/signUp")
    public String doSignUp(@Validated(ValidationGroupSequence.class) @ModelAttribute("client") SignUpClientForm form, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "client/newClient";
        }
        signUpClientService.save(form.getName(), form.getEmail(), form.getPassword(), form.getCard());
        redirectAttributes.addFlashAttribute("message", "You are successfully registered");
        return "redirect:/login";
    }
}
