package com.example.rd.autocode.assessment.appliances.appliance.find;

import com.example.rd.autocode.assessment.appliances.appliance.ApplianceRepository;
import com.example.rd.autocode.assessment.appliances.appliance.ApplianceSort;
import com.example.rd.autocode.assessment.appliances.appliance.Category;
import com.example.rd.autocode.assessment.appliances.appliance.PowerType;
import com.example.rd.autocode.assessment.appliances.misc.infrastructure.logging.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.EnumSet;
import java.util.List;

@Controller
@RequestMapping("/appliances")
@RequiredArgsConstructor
public class FindApplianceController {
    private final ApplianceRepository repository;
    private final VectorStore vectorStore;
    @GetMapping
    @Loggable
    public String findAllBySpecification(@SortDefault.SortDefaults({@SortDefault("name"), @SortDefault("id")}) Pageable pageable, Model model, @ModelAttribute("searchForm") ApplianceSearchForm searchForm) {
        model.addAttribute("appliances", repository.findAll(searchForm.toSpecification(), pageable));
        return "appliance/appliances";
    }

//    @GetMapping("/search")
//    public String findAllByDescriptionContent(Model model, @RequestParam String text, Pageable pageable) {
//        model.addAttribute("appliances", repository.findByDescriptionContaining(text, pageable));
//        return "appliance/appliances";
//    }

    @GetMapping("/search")
    public String semanticSearchAllByDescription(Model model, @RequestParam String text, Pageable pageable) {
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.query(text).withSimilarityThreshold(0.7d).withTopK(4));
        List<Long> ids = documents.stream().map(d -> ((Long) d.getMetadata().get("id"))).toList();
        model.addAttribute("appliances", new PageImpl<>(repository.findAllById(ids), pageable, ids.size()));
        return "appliance/appliances";
    }

    @ModelAttribute
    public void populateModel(Model model) {
        model.addAttribute("powerTypes", EnumSet.allOf(PowerType.class));
        model.addAttribute("categories", EnumSet.allOf(Category.class));
        model.addAttribute("sorts", EnumSet.allOf(ApplianceSort.class));
        model.addAttribute("searchForm", new ApplianceSearchForm());

    }
}

