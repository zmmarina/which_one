package com.zm.which_one.controller;

import com.zm.which_one.model.Book;
import com.zm.which_one.service.BookService;
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
    public Book suggestBook(@RequestBody String prefrences){
        return bookService.suggest(prefrences);
    }
}
