package com.example.rd.autocode.assessment.appliances.appliance.manage;

import com.example.rd.autocode.assessment.appliances.appliance.*;
import com.example.rd.autocode.assessment.appliances.appliance.find.ApplianceSearchForm;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.ManufacturerRepository;
import com.example.rd.autocode.assessment.appliances.misc.infrastructure.web.EditMode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/appliances")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('EMPLOYEE')")
public class ManageApplianceController {
    ManageApplianceService applianceService;
    ManufacturerRepository manufacturerRepo;

    @GetMapping("/create")
    public String getCreationForm(Model model) {
        model.addAttribute("appliance", new CreateApplianceForm());
        model.addAttribute("editMode", EditMode.CREATE);
        return "appliance/newAppliance";
    }

    @GetMapping("/{id}/edit")
    public String getEditForm(@PathVariable("id") Long id, Model model) {
        Appliance appliance = applianceService.findById(id);
        model.addAttribute("appliance", ApplianceMapper.INSTANCE.toDto(appliance));
        model.addAttribute("editMode", EditMode.UPDATE);

        return "appliance/newAppliance";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("appliance") CreateApplianceForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.CREATE);
            return "appliance/newAppliance";
        }
        applianceService.create(form.toParameters());
        redirectAttributes.addFlashAttribute("message", "Appliance %s is created successfully".formatted(form.getName()));
        return "redirect:/appliances";
    }

    @PostMapping("/{id}/edit")
    public String edit(@Validated @ModelAttribute("appliance") EditApplianceForm form,
                       BindingResult bindingResult,
                       Model model,
                       @PathVariable Long id,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.UPDATE);
            return "appliance/newAppliance";
        }
        applianceService.edit(id, EditApplianceParametersMapper.INSTANCE.toDto(form));
        redirectAttributes.addFlashAttribute("message", "Appliance %s is successfully edited".formatted(form.getName()));
        return "redirect:/appliances";
    }

    @PostMapping("/{id}/delete")
    public String delete(Model model,
                       @PathVariable Long id,
                       RedirectAttributes redirectAttributes) {
        applianceService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Appliance is successfully deleted");
        return "redirect:/appliances";
    }

    @ModelAttribute
    public void populateModel(Model model) {
        model.addAttribute("powerTypes", PowerType.values());
        model.addAttribute("manufacturers", manufacturerRepo.findAll());
        model.addAttribute("categories", Category.values());
        model.addAttribute("sorts", ApplianceSort.values());
        model.addAttribute("searchForm", new ApplianceSearchForm());
    }
}

