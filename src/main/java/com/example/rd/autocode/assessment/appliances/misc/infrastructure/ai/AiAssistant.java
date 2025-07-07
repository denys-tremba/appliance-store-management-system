package com.example.rd.autocode.assessment.appliances.misc.infrastructure.ai;

import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.content.Content;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class AiAssistant {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;

    @SneakyThrows
    public AiAssistant(ChatClient.Builder builder, VectorStore vectorStore, AiTools aiTools, ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .similarityThreshold(0.95)
                .topK(10)
                .vectorStore(vectorStore)
                .build();
        PromptTemplate promptTemplate = PromptTemplate.builder().template(new ClassPathResource("ai/rag.st").getContentAsString(StandardCharsets.UTF_8)).build();
        ContextualQueryAugmenter queryAugmenter = ContextualQueryAugmenter.builder()
                .documentFormatter(new DocumentFormatter())
                .promptTemplate(promptTemplate).build();
        BaseAdvisor ragAdvisor = new QuestionAnswerAdvisor(vectorStore);
//        RetrievalAugmentationAdvisor ragAdvisor = RetrievalAugmentationAdvisor
//                .builder()
//                .order(Ordered.HIGHEST_PRECEDENCE + 1)
//                .queryAugmenter(queryAugmenter)
//                .documentRetriever(documentRetriever)
//                .build();
        BaseChatMemoryAdvisor chatMemoryAdvisor = PromptChatMemoryAdvisor.builder(chatMemory)
//                .order(Ordered.HIGHEST_PRECEDENCE + 1)
                .build();
        SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE);
        chatClient = builder
                .defaultTools(aiTools)
                .defaultSystem(new ClassPathResource("ai/system.st"))
                .defaultAdvisors(ragAdvisor, simpleLoggerAdvisor, chatMemoryAdvisor)
                .build();
        this.vectorStore = vectorStore;
    }

    public String promptForResponse(String query, User user, Map<String, Object> context) {
        return chatClient.prompt()
                .user(query)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID, user.getUsername()))
                .toolContext(context)
                .call()
                .content();
    }

    public List<String> retrieveMemoryForUser(User user) {
        return chatMemory.get(user.getUsername()).stream().map(Content::getText).toList();
    }
}
