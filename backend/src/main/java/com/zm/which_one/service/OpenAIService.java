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
public class OpenAIService {

    private final WebClient webClient;
    private final String apiKey;

    private static final Map<String, String> SYSTEM_MESSAGE = Map.of(
            "role", "system",
            "content", "You are a helpful assistant that recommends books based on user preferences."
    );

    public OpenAIService(){
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("OPENAI_API_KEY");

        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String askLLM(String userPrompt){
        Map<String, Object> body = Map.of(
                "model", "gpt-4o",
                "messages", new Object[]{
                        SYSTEM_MESSAGE,
                        Map.of("role", "user", "content", userPrompt)
                }
        );

        try{
            ChatResponse response = webClient.post()
                    .uri("/chat/completions")
                    .body(Mono.just(body), Map.class)
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .retryWhen(
                            reactor.util.retry.Retry.backoff(3, java.time.Duration.ofSeconds(2))
                                    .filter(ex -> ex instanceof org.springframework.web.reactive.function
                                            .client.WebClientResponseException.TooManyRequests)
                    )
                    .block();

            if(response != null && response.choices() != null && !response.choices().isEmpty()){
               return response.choices().get(0).message().content();
            }
            return "No response from model";

        } catch(Exception e){
            e.printStackTrace();
            return "Error communicating with the LLM" + e.getMessage();
        }
    }
}
