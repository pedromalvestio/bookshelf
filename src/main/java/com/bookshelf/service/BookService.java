package com.bookshelf.service;

import com.bookshelf.model.Book;
import com.bookshelf.model.User;
import com.bookshelf.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRepository repository;

    @Autowired
    UserService userService;

    public Book create(Long userId, Book book){
        book.setUser(userService.userLogged(userId));
        return repository.save(book);
    }

    public List<Book> getBooks(Long userId) {
        userService.userLogged(userId);
        return repository.findAllByUserId(userId);
    }

    public List<Book> getBookByParam(Long userId, String title, String author) {

        userService.userLogged(userId);

        if (title != null && author != null)
            return repository.findByUserIdAndTitleAndAuthor(userId, title, author);
        if (author != null) return repository.findByUserIdAndAuthor(userId, author);
        return repository.findByUserIdAndTitle(userId, title);
    }
}
