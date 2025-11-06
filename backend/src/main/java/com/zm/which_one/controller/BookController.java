package com.zm.which_one.controller;

import com.zm.which_one.model.Book;
import com.zm.which_one.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @PostMapping("/suggest")
    public ResponseEntity<?> suggestBook(@RequestBody String preferences){
        try {
            Book suggestedBook = bookService.suggest(preferences);

            if(suggestedBook == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sorry... No book suggestion could be generated.");
            }
            return ResponseEntity.ok(suggestedBook);

        } catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error parsing model response: " + e.getMessage());

        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
