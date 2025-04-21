package com.inshorts.newsRetrieval.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.theokanning.openai.service.OpenAiService;

import java.util.List;

@Service
public class LLMService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private OpenAiService openAiService;

    @PostConstruct
    public void init() {
        openAiService = new OpenAiService(openaiApiKey);
    }

    @Cacheable(value = "llm-summary", key = "#description.hashCode()")
    public String getLLMSummary(String description) {
        String prompt = buildPrompt(description);
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4o-mini")
                .messages(List.of(new ChatMessage("user", prompt)))
                .temperature(0.2)
                .build();

        ChatCompletionResult result = openAiService.createChatCompletion(request);

        return result.getChoices().get(0).getMessage().getContent();
    }

    private String buildPrompt(String description) {
        return String.format("""
        Summarize the following news article in 2-3 sentences:
        
        Description: %s
        """, description);
    }
}
