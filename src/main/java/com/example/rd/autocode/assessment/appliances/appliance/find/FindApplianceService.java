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
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${search.threshold:0.7}")
    private double threshold;
    @Value("${search.topK:4}")
    private int topK;
    public Page<Appliance> semanticSearchAllByDescription(String description) {
        SearchRequest searchRequest = SearchRequest.builder().query(description).similarityThreshold(threshold).topK(topK).build();
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        List<Appliance> ids = documents
                .stream()
                .map(d -> ((Long) d.getMetadata().get("id")))
                .map(applianceRepository::findById)
                .flatMap(Optional::stream)
                .toList();
        PageImpl<Appliance> page = new PageImpl<>(ids, Pageable.unpaged(), ids.size());
        return page;
    }

    public Page<Appliance> semanticSearchAllByDescriptionAndCategory(String description, Category category, BigDecimal budget) {
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression expression = b.and(b.eq("category", category.name()), b.lte("price", budget.longValue())).build();
        SearchRequest searchRequest = SearchRequest.builder()
                .query(description)
                .similarityThreshold(threshold)
                .topK(topK)
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
        return page;
    }
}
