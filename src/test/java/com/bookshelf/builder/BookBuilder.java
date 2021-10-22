package com.bookshelf.builder;

import com.bookshelf.model.Book;
import com.bookshelf.model.User;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class BookBuilder {
    public static GenericBuilder<Book> defaults() {
        return GenericBuilder.of(() -> createObjectDefault());
    }

    public static GenericBuilder<Book> withAuthor() {
        GenericBuilder<Book> builder = GenericBuilder.of(() -> createObjectDefault());
        builder.with(Book::setAuthor, randomAlphanumeric(100));
        return builder;
    }

    public static GenericBuilder<Book> withAuthor(String author) {
        GenericBuilder<Book> builder = GenericBuilder.of(() -> createObjectDefault());
        builder.with(Book::setAuthor, author);
        return builder;
    }

    public static GenericBuilder<Book> withUser(User user) {
        GenericBuilder<Book> builder = GenericBuilder.of(() -> createObjectDefault());
        builder.with(Book::setUser, user);
        return builder;
    }

    public static GenericBuilder<Book> withUser() {
        GenericBuilder<Book> builder = GenericBuilder.of(() -> createObjectDefault());
        builder.with(Book::setUser, UserBuilder.defaults().build());
        return builder;
    }
    public static GenericBuilder<Book> withUserAndAuthor() {
        GenericBuilder<Book> builder = GenericBuilder.of(() -> createObjectDefault());
        builder.with(Book::setUser, UserBuilder.defaults().build());
        builder.with(Book::setAuthor, randomAlphanumeric(100));
        return builder;
    }

    private static Book createObjectDefault() {
        Book book = new Book();
        book.setTitle(randomAlphanumeric(100));
        book.setCreatedDate(Date.from(Instant.now()));
        return book;
    }

    public static GenericBuilder<Book> withAuthorAndUser(User user) {
        GenericBuilder<Book> builder = GenericBuilder.of(() -> createObjectDefault());
        builder.with(Book::setUser, user);
        builder.with(Book::setAuthor, randomAlphanumeric(100));
        return builder;
    }
}
