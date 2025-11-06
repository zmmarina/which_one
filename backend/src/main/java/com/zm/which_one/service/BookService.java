package com.zm.which_one.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.which_one.model.Book;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final ObjectMapper objectMapper;
    private final OpenAIService openAIService;

    public BookService(ObjectMapper objectMapper, OpenAIService openAIService) {
        this.objectMapper = objectMapper;
        this.openAIService = openAIService;
    }

    public Book suggest(String preferences) {
        String prompt = createPrompt(preferences);
        String llmReturn = openAIService.askLLM(prompt);

        return responseParse(llmReturn);
    }

    private String createPrompt(String preferences) {
        return """
                You are an assistant that suggests romance books. 
                The user said: %s
                Based on this, suggest a single book that best matches their preferences.
                Important:
                    - Respond ONLY with a valid JSON object.
                    - Do NOT include any explanations, comments, or text outside the JSON.
                    - The JSON must match exactly this structure:
                    {
                        "title": "The book title",
                        "summary": "A brief summary of the story, 2-3 sentences max",
                        "storeLink": "A link to purchase the book on Amazon",
                        "tags": ["genre", "theme", "trope1", "trope2"]
                }
                Make sure:
                - The JSON is valid and parsable
                - Tags are short and descriptive
                - Do not include any extra text outside the JSON
                """.formatted(preferences);
    }


    private Book responseParse(String llmReturn) {
        try {
            return objectMapper.readValue(llmReturn, Book.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse LLM response", e);
        }
    }

}
