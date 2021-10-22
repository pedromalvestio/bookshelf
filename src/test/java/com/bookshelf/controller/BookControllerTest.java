package com.bookshelf.controller;

import com.bookshelf.builder.BookBuilder;
import com.bookshelf.builder.UserBuilder;
import com.bookshelf.model.Book;
import com.bookshelf.model.User;
import com.bookshelf.repository.BookRepository;
import com.bookshelf.repository.UserRepository;
import com.bookshelf.service.BookService;
import com.bookshelf.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = {BookController.class, BookService.class, UserService.class})
public class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookRepository repository;

    @MockBean
    UserRepository userRepository;

    @Captor
    private ArgumentCaptor<Book> captor;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldCreateBookForLoggedUser() throws Exception {
        User user = UserBuilder.defaults().build();
        Book newBook = BookBuilder.defaults().build();

        Long bookId = nextLong(1,1000);
        Long userId = nextLong(1, 1000);
        Date createdDate = Date.from(Instant.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(repository.save(captor.capture())).thenAnswer(b ->{
            Book book = b.getArgument(0, Book.class);
            book.setId(bookId);
            book.setCreatedDate(createdDate);
            return book;
        });

        ResultActions post = mockMvc.perform(post("/{userId}/book", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)));

        post.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(bookId))
                .andExpect(jsonPath("title").value(newBook.getTitle()))
                .andExpect(jsonPath("author").value(newBook.getAuthor()))
                .andExpect(jsonPath("user.username").value(user.getUsername()));;

        Book book = captor.getValue();
        assertEquals(book.getId(), bookId);
        assertEquals(book.getTitle(), newBook.getTitle());
        assertEquals(book.getAuthor(), newBook.getAuthor());
        assertEquals(book.getCreatedDate(), createdDate);
        assertEquals(book.getUser(), user);
    }

    @Test
    public void shouldNotCreateBookForNotLoggedUser() throws Exception {
        User user = UserBuilder.defaults().build();
        user.setLogged(false);
        Book newBook = BookBuilder.defaults().build();

        Long bookId = nextLong(1,1000);
        Long userId = nextLong(1, 1000);
        Date createdDate = Date.from(Instant.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(repository.save(captor.capture())).thenAnswer(b ->{
            Book book = b.getArgument(0, Book.class);
            book.setId(bookId);
            book.setCreatedDate(createdDate);
            return book;
        });

        ResultActions post = mockMvc.perform(post("/{userId}/book", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)));

        post.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBooksForLoggedUser() throws Exception {
        User user = UserBuilder.defaults().build();
        Book book = BookBuilder.withUser(user).build();

        when(repository.findAllByUserId(user.getId())).thenReturn(Arrays.asList(book));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ResultActions get = mockMvc.perform(get("/{userId}/book", user.getId()));

        get.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].title").value(book.getTitle()))
                .andExpect(jsonPath("[0].author").value(book.getAuthor()));
    }

    @Test
    public void shouldNotReturnBooksForNotLoggedUser() throws Exception {
        User user = UserBuilder.defaults().build();
        user.setLogged(false);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ResultActions get = mockMvc.perform(get("/{userId}/book", user.getId()));

        get.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBookByTitle() throws Exception {
        User user = UserBuilder.defaults().build();
        Book book = BookBuilder.withUser(user).build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(repository.findByUserIdAndTitle(user.getId(), book.getTitle())).thenReturn(Arrays.asList(book));

        ResultActions get = mockMvc.perform(get("/{userId}/book?title={title}", user.getId(), book.getTitle()));

        get.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].title").value(book.getTitle()));
    }

    @Test
    public void shouldReturnBookByAuthor() throws Exception {
        User user = UserBuilder.defaults().build();
        Book bookOne = BookBuilder.withAuthor().build();
        bookOne.setUser(user);
        Book bookTwo = BookBuilder.withAuthor(bookOne.getAuthor()).build();
        bookTwo.setUser(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(repository.findByUserIdAndAuthor(user.getId(), bookOne.getAuthor())).thenReturn(Arrays.asList(bookOne, bookTwo));

        ResultActions get = mockMvc.perform(get("/{userId}/book?author={author}", user.getId(), bookOne.getAuthor()));

        get.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].title").value(bookOne.getTitle()))
                .andExpect(jsonPath("[0].author").value(bookOne.getAuthor()))
                .andExpect(jsonPath("[1].title").value(bookTwo.getTitle()))
                .andExpect(jsonPath("[1].author").value(bookTwo.getAuthor()));
    }

    @Test
    public void shouldReturnBookByTitleAndAuthor() throws Exception {
        User user = UserBuilder.defaults().build();
        Book book = BookBuilder.withAuthor().build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(repository.findByUserIdAndTitleAndAuthor(user.getId(), book.getTitle(), book.getAuthor())).thenReturn(Arrays.asList(book));

        ResultActions get = mockMvc.perform(get("/{userId}/book?title={title}&author={author}",
                user.getId(), book.getTitle(), book.getAuthor()));

        get.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].title").value(book.getTitle()))
                .andExpect(jsonPath("[0].author").value(book.getAuthor()));
    }

}
