package com.zm.which_one.service;

import com.zm.which_one.model.ChatResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;


@Service
public class GeminiService {

    private final WebClient webClient;
    private final String apiKey;

    public GeminiService() {
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("GEMINI_API_KEY");

        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String askLLM(String userPrompt) {
        Map<String, Object> body = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", userPrompt)
                        })
                }
        );

        try {
            ChatResponse response = webClient.post()
                    .uri("/models/gemini-2.5-flash:generateContent?key=" + apiKey)
                    .body(Mono.just(body), Map.class)
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .block();

            if (response != null && response.candidates() != null && !response.candidates().isEmpty()) {
                return response
                        .candidates().get(0)
                        .content().parts().get(0)
                        .text();
            }
            return "No response from model";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error communicating with the LLM" + e.getMessage();
        }
    }
}
