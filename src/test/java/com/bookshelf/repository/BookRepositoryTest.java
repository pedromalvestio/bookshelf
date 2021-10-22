package com.bookshelf.repository;

import com.bookshelf.builder.BookBuilder;
import com.bookshelf.model.Book;
import com.bookshelf.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@TestPropertySource("file:src/test/resources/test.properties")
@DataJpaTest
@Sql("file:src/test/resources/database.sql")
public class BookRepositoryTest {

    @Autowired
    BookRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void shouldSaveABook(){
        Book book = BookBuilder.withUser().build();
        User user = entityManager.merge(book.getUser());
        user = entityManager.persistAndFlush(user);
        book.setUser(user);
        book = repository.saveAndFlush(book);
        entityManager.detach(book);
        Book findBook = repository.getById(book.getId());
        assertEquals(book.getTitle(), findBook.getTitle());
        assertEquals(book.getAuthor(), findBook.getAuthor());
        assertEquals(findBook.getUser(), user);
    }

    @Test
    public void shouldReturnUserBooks(){
        Book bookOne = BookBuilder.withUser().build();
        User user = entityManager.merge(bookOne.getUser());
        user = entityManager.persistAndFlush(user);
        bookOne.setUser(user);
        bookOne = repository.saveAndFlush(bookOne);
        Book bookTwo = BookBuilder.withUser(user).build();
        bookTwo = repository.saveAndFlush(bookTwo);
        entityManager.detach(bookOne);
        entityManager.detach(bookTwo);

        List<Book> allBooks = repository.findAllByUserId(user.getId());
        assertEquals(bookOne.getTitle(), allBooks.get(0).getTitle());
        assertEquals(bookOne.getAuthor(), allBooks.get(0).getAuthor());
        assertEquals(user, allBooks.get(0).getUser());
        assertEquals(bookTwo.getTitle(), allBooks.get(1).getTitle());
        assertEquals(bookTwo.getAuthor(), allBooks.get(1).getAuthor());
        assertEquals(user, allBooks.get(1).getUser());
    }

    @Test
    public void shouldReturnUserBooksByTitle() {
        Book bookOne = BookBuilder.withUser().build();
        User user = entityManager.merge(bookOne.getUser());
        user = entityManager.persistAndFlush(user);
        bookOne.setUser(user);
        bookOne = repository.saveAndFlush(bookOne);
        Book bookTwo = BookBuilder.withUser(user).build();
        bookTwo = repository.saveAndFlush(bookTwo);
        entityManager.detach(bookOne);
        entityManager.detach(bookTwo);

        List<Book> allBooks = repository.findByUserIdAndTitle(user.getId(), bookOne.getTitle());
        assertEquals(1, allBooks.size());
        assertEquals(bookOne.getTitle(), allBooks.get(0).getTitle());
        assertEquals(bookOne.getAuthor(), allBooks.get(0).getAuthor());
        assertEquals(user, allBooks.get(0).getUser());
    }

    @Test
    public void shouldReturnUserBooksByAuthor() {
        Book bookOne = BookBuilder.withUserAndAuthor().build();
        User user = entityManager.merge(bookOne.getUser());
        user = entityManager.persistAndFlush(user);
        bookOne.setUser(user);
        bookOne = repository.saveAndFlush(bookOne);
        Book bookTwo = BookBuilder.withAuthorAndUser(user).build();
        bookTwo = repository.saveAndFlush(bookTwo);
        entityManager.detach(bookOne);
        entityManager.detach(bookTwo);

        List<Book> allBooks = repository.findByUserIdAndAuthor(user.getId(), bookTwo.getAuthor());
        assertEquals(1, allBooks.size());
        assertEquals(bookTwo.getTitle(), allBooks.get(0).getTitle());
        assertEquals(bookTwo.getAuthor(), allBooks.get(0).getAuthor());
        assertEquals(user, allBooks.get(0).getUser());
    }

    @Test
    public void shouldReturnUserBooksByTitleAndAuthor() {
        Book bookOne = BookBuilder.withUserAndAuthor().build();
        User user = entityManager.merge(bookOne.getUser());
        user = entityManager.persistAndFlush(user);
        bookOne.setUser(user);
        bookOne = repository.saveAndFlush(bookOne);
        Book bookTwo = BookBuilder.withAuthorAndUser(user).build();
        bookTwo = repository.saveAndFlush(bookTwo);
        entityManager.detach(bookOne);
        entityManager.detach(bookTwo);

        List<Book> allBooks = repository.findByUserIdAndTitleAndAuthor(user.getId(), bookTwo.getTitle(), bookTwo.getAuthor());
        assertEquals(1, allBooks.size());
        assertEquals(bookTwo.getTitle(), allBooks.get(0).getTitle());
        assertEquals(bookTwo.getAuthor(), allBooks.get(0).getAuthor());
        assertEquals(user, allBooks.get(0).getUser());
    }
}
