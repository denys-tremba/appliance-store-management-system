package com.example.rd.autocode.assessment.appliances.misc.infrastructure.ai;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DocumentFormatter implements Function<List<Document>, String> {
    @Override
    public String apply(List<Document> documents) {
        return documents.stream()
                .map(this::mapDocumentToLine)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String mapDocumentToLine(Document document) {
        return document.getFormattedContent(MetadataMode.ALL);
    }
}
