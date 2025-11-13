package com.zm.which_one.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.which_one.model.Book;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final ObjectMapper objectMapper;
    private final GeminiService geminiService;

    public BookService(ObjectMapper objectMapper, GeminiService geminiService) {
        this.objectMapper = objectMapper;
        this.geminiService = geminiService;
    }

    public Book suggest(String preferences) {
        String prompt = createPrompt(preferences);
        String llmReturn = geminiService.askLLM(prompt);

        return responseParse(llmReturn);
    }

    private String createPrompt(String preferences) {
        return """
                You are an assistant that suggests romance books to users. 
                The user said: %s
                
                Your task:
                        - Detect automatically the language in which the user wrote.
                        - Respond in the SAME language (title, summary, tags, everything).
                        - Suggest ONE book that best matches the user's preferences.
                        
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
               Requirements:
                - The JSON is valid and parsable
                - All text values (title, summary, tags) must be in the same language as the user input.
                - Tags are short and descriptive
                - Do not include any extra text outside the JSON
                """.formatted(preferences);
    }


    private Book responseParse(String llmReturn) {
        try {
            String json = llmReturn
                    .replaceAll("(?s).*?\\{", "{")
                    .replaceAll("}[^}]*$", "}");
            return objectMapper.readValue(json, Book.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse LLM response:" + llmReturn, e);
        }
    }

}
