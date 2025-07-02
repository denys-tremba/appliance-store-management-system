package com.example.rd.autocode.assessment.appliances.appliance;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
public class AiAssistant {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public AiAssistant(ChatClient.Builder builder, VectorStore vectorStore) {
        chatClient = builder
                .defaultSystem("""
                        Advice appropriate appliances given desired functionality
                        """)
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
        this.vectorStore = vectorStore;
    }

    public String chat(String query) {
        return chatClient.prompt()
                .user(query)
                .call()
                .content();
    }
}
