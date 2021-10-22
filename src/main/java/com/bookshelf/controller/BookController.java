package com.bookshelf.controller;

import com.bookshelf.model.Book;
import com.bookshelf.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/{userId}/book")
    public ResponseEntity getBook(@PathVariable Long userId,
                                  @RequestParam(required = false) String title,
                                  @RequestParam(required = false) String author){
        if (title != null || author != null) return ResponseEntity.ok(bookService.getBookByParam(userId, title, author));
        return ResponseEntity.ok(bookService.getBooks(userId));
    }

    @PostMapping("/{userId}/book")
    public ResponseEntity createBook(@PathVariable Long userId, @RequestBody Book book){

        return ResponseEntity.ok(bookService.create(userId, book));
    }
}
