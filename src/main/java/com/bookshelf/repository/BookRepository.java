package com.bookshelf.repository;

import com.bookshelf.model.Book;
import com.bookshelf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByUserId(Long userId);

    List<Book> findByUserIdAndTitleAndAuthor(Long userId, String title, String author);

    List<Book> findByUserIdAndAuthor(Long userId, String author);

    List<Book> findByUserIdAndTitle(Long userId, String title);
}
