package com.example.rd.autocode.assessment.appliances.manufacturer.manage;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.web.EditMode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {
    private final ManageManufacturerService service;

    @GetMapping
    public String findAll(Model model, @SortDefault.SortDefaults(@SortDefault("name")) Pageable pageable) {
        model.addAttribute("manufacturers", service.findAll(pageable));
        return "manufacturer/list";
    }
    @GetMapping("/create")
    public String getCreationForm(Model model) {
        model.addAttribute("manufacturer", new EditManufacturerForm());
        model.addAttribute("editMode", EditMode.CREATE);
        return "manufacturer/edit";
    }

    @PostMapping("/create")
    public String doCreate(@Validated @ModelAttribute("manufacturer") EditManufacturerForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.CREATE);
            return "manufacturer/edit";
        }
        service.create(form.getName());
        redirectAttributes.addFlashAttribute("message", "Manufacturer is created");
        return "redirect:/manufacturers";
    }
    @PostMapping("/{id}/delete")
    public String doDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        service.delete(id);
        redirectAttributes.addFlashAttribute("message", "Manufacturer is deleted");
        return "redirect:/manufacturers";
    }
    @GetMapping("/{id}/edit")
    public String getEditForm(@PathVariable Long id, Model model) {
        Manufacturer manufacturer = service.findById(id);
        model.addAttribute("editMode", EditMode.UPDATE);
        model.addAttribute("manufacturer", EditManufacturerForm.from(manufacturer));
        return "manufacturer/edit";
    }
    @PostMapping("/{id}/edit")
    public String doEdit(@PathVariable Long id, @Validated @ModelAttribute("manufacturer") EditManufacturerForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.UPDATE);
            return "manufacturer/edit";
        }
        service.edit(id, form.getName());
        redirectAttributes.addFlashAttribute("message", "Manufacturer is successfully edited");
        return "redirect:/manufacturers";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView onIntegrityViolation(DataIntegrityViolationException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/4xx");
        modelAndView.addObject("error", "You can not delete/edit manufacturer because there exists appliance referencing it");
        modelAndView.addObject("url", "/manufacturers");
        return modelAndView;
    }
}
