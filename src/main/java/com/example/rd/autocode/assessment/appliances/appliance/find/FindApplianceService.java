package com.example.rd.autocode.assessment.appliances.appliance.find;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.appliance.ApplianceRepository;
import com.example.rd.autocode.assessment.appliances.appliance.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindApplianceService {
    private final VectorStore vectorStore;
    private final ApplianceRepository applianceRepository;
    public Page<Appliance> semanticSearchAllByDescription(String description) {
        SearchRequest searchRequest = SearchRequest.builder().query(description).similarityThreshold(0.7d).topK(4).build();
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        List<Appliance> ids = documents
                .stream()
                .map(d -> ((Long) d.getMetadata().get("id")))
                .map(applianceRepository::findById)
                .flatMap(Optional::stream)
                .toList();
        PageImpl<Appliance> page = new PageImpl<>(ids, Pageable.unpaged(), ids.size());
//        List<Long> ids = documents.stream().map(d -> ((Long) d.getMetadata().get("id"))).toList();
//        PageImpl<Appliance> page = new PageImpl<>(applianceRepository.findAllById(ids), Pageable.unpaged(), ids.size());
        return page;
    }

    public Page<Appliance> semanticSearchAllByDescriptionAndCategory(String description, Category category, BigDecimal budget) {
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression expression = b.and(b.eq("category", category.name()), b.lte("price", budget.longValue())).build();
        SearchRequest searchRequest = SearchRequest.builder()
                .query(description)
//                .similarityThreshold(0.85d)
//                .topK(10)
                .filterExpression(expression)
                .build();
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        List<Appliance> ids = documents
                .stream()
                .map(d -> ((Long) d.getMetadata().get("id")))
                .map(applianceRepository::findById)
                .flatMap(Optional::stream)
                .toList();
        PageImpl<Appliance> page = new PageImpl<>(ids, Pageable.unpaged(), ids.size());
//        List<Long> ids = documents.stream().map(d -> ((Long) d.getMetadata().get("id"))).toList();
//        PageImpl<Appliance> page = new PageImpl<>(applianceRepository.findAllById(ids), Pageable.unpaged(), ids.size());
        return page;
    }
}
