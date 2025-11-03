package com.zm.which_one.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.which_one.model.Book;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final ObjectMapper objectMapper;

    public BookService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Book suggest(String preferences) {
        String prompt = createPrompt(preferences);
        String llmReturn = callLLM(prompt);

        return responseParse(llmReturn);
    }

    private String createPrompt(String preferences) {
        return """
                You are an assistant that suggests romance books. 
                The user said: %s
                Based on this, suggest a single book that best matches their preferences.
                Respond **exactly** in the following JSON format:
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

    private String callLLM(String prompt) {
        return """
                {
                    "title": "Taken by the mobster",
                    "summary": "A gripping tale of power, passion, and the perilous world of the mafia...",
                    "storeLink": "https://www.amazon.com.br/dp/B08XYZ123",
                    "tags": ["mafia", "hot", "enemies to lovers", "age gap"]
                }
                """;
    }

    private Book responseParse(String llmReturn) {
        try {
            return objectMapper.readValue(llmReturn, Book.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse LLM response", e);
        }
    }

}
