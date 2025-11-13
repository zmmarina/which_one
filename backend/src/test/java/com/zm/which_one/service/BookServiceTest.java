package com.zm.which_one.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.which_one.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {

    private BookService bookService;
    private GeminiService geminiService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        geminiService = Mockito.mock(GeminiService.class);
        objectMapper = new ObjectMapper();
        bookService = new BookService(objectMapper,geminiService);
    }

    @Test
    void shouldReturnValidBookWhenGeminiRespondsValidJson(){
        String preferences = "romance between enemies";
        String response = """
                {
                    "title": "Enemies to Lovers",
                    "summary": "A thrilling story where love blooms amid rivalry.",
                    "storeLink": "https://www.amazon.com/enemies-to-lovers",
                    "tags": ["romance", "rivals", "slow burn"]
                }
                """;

        Mockito.when(geminiService.askLLM(Mockito.anyString()))
                .thenReturn(response);

        Book book = bookService.suggest(preferences);

        assertNotNull(book);
        assertEquals("Enemies to Lovers", book.title());
        assertTrue(book.tags().contains("romance"));
    }

    @Test
    void shouldParseBookEvenIfResponseHasExtraText(){
        String preferences = "romance between enemies";
        String response = """
            Sure! Here's a great suggestion:
            {
                "title": "A Court of Thorns and Roses",
                "summary": "A blend of fantasy and passion, where enemies turn to lovers.",
                "storeLink": "https://www.amazon.com/acotar",
                "tags": ["fantasy", "romance", "enemies to lovers"]
            }
            Enjoy your reading!
            """;

        Mockito.when(geminiService.askLLM((Mockito.anyString())))
                .thenReturn(response);

        Book book = bookService.suggest(preferences);

        assertNotNull(book);
        assertEquals("A Court of Thorns and Roses", book.title());
    }

    @Test
    void shouldHandlePartialResponse(){
        String preferences = "romance between enemies";
        String response = """
            Sure! Here's a great suggestion:
            {
                "title": "A Court of Thorns and Roses",
                "summary": "A blend of fantasy and passion, where enemies turn to lovers."
            }
            """;

        Mockito.when(geminiService.askLLM(Mockito.anyString()))
                .thenReturn(response);

        Book book = bookService.suggest(preferences);

        assertNotNull(book);
        assertEquals("A Court of Thorns and Roses", book.title());
        assertNull(book.storeLink());
    }

    @Test
    void shouldKeepResponseSameLanguageAsInput(){
        String preferences = "Quero um romance entre inimigos";
        String response = """
            {
                "title": "Inimigos e Amantes",
                "summary": "Uma história de paixão e rivalidade entre dois mundos opostos.",
                "storeLink": "https://www.amazon.com/inimigos-e-amantes",
                "tags": ["romance", "inimigos", "paixão"]
            }
            """;

        Mockito.when(geminiService.askLLM(Mockito.anyString()))
                .thenReturn(response);

        Book book = bookService.suggest(preferences);

        assertTrue(book.summary().contains("paixão"));
    }

    @Test
    void shouldThrowExceptionWhenResponseInvalid(){
        String preferences = "romance between enemies";

        Mockito.when(geminiService.askLLM(Mockito.anyString()))
                .thenReturn("invalid_json");

        assertThrows(RuntimeException.class, ()-> bookService.suggest(preferences));
    }
}

