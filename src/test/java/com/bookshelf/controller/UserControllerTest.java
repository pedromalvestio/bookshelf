package com.bookshelf.controller;

import com.bookshelf.builder.UserBuilder;
import com.bookshelf.dto.LoginDTO;
import com.bookshelf.model.User;
import com.bookshelf.repository.UserRepository;
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

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = {UserController.class, UserService.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repository;

    @Captor
    private ArgumentCaptor<User> captor;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldCreateUser() throws Exception {
        User newUser = UserBuilder.defaults().build();

        Long userId = nextLong(1,1000);

        when(repository.save(captor.capture())).thenAnswer(u -> {
            User user = u.getArgument(0, User.class);
            user.setId(userId);
            return user;
        });

        ResultActions post = mockMvc.perform(post("/user").
                contentType(MediaType.APPLICATION_JSON).
                content(mapper.writeValueAsString(newUser)));

        post.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(userId))
                .andExpect(jsonPath("username").value(newUser.getUsername()))
                .andExpect(jsonPath("password").value(newUser.getPassword()));

        User user = captor.getValue();
        assertEquals(userId, user.getId());
        assertEquals(newUser.getUsername(), user.getUsername());
        assertEquals(newUser.getPassword(), user.getPassword());
    }

    @Test
    public void shouldLoginUser() throws Exception {
        User loginUser = UserBuilder.defaults().build();
        loginUser.setLogged(false);
        LoginDTO login = new LoginDTO();
        login.setUser(loginUser.getUsername());
        login.setPassword(loginUser.getPassword());

        when(repository.save(captor.capture())).thenAnswer(u -> {
            User user = u.getArgument(0, User.class);
            return user;
        });
        when(repository.findByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword())).thenReturn(loginUser);

        ResultActions post = mockMvc.perform(post("/user/login").
                contentType(MediaType.APPLICATION_JSON).
                content(mapper.writeValueAsString(login)));

        post.andDo(print())
                .andExpect(status().isOk());

        User user = captor.getValue();
        assertEquals(user.getLogged(), true);
    }

    @Test
    public void shouldNotLoginInvalidUser() throws Exception {
        User loginUser = UserBuilder.defaults().build();

        LoginDTO login = new LoginDTO();
        login.setUser(loginUser.getUsername());
        login.setPassword(loginUser.getPassword());


        ResultActions post = mockMvc.perform(post("/user/login").
                contentType(MediaType.APPLICATION_JSON).
                content(mapper.writeValueAsString(login)));

        post.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotLoginLoggedUser() throws Exception {
        User loginUser = UserBuilder.defaults().build();
        loginUser.setLogged(true);
        LoginDTO login = new LoginDTO();
        login.setUser(loginUser.getUsername());
        login.setPassword(loginUser.getPassword());

        when(repository.findByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword())).thenReturn(loginUser);

        ResultActions post = mockMvc.perform(post("/user/login").
                contentType(MediaType.APPLICATION_JSON).
                content(mapper.writeValueAsString(login)));

        post.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldLogoutUser() throws Exception {
        User loginUser = UserBuilder.defaults().build();
        loginUser.setLogged(true);
        LoginDTO login = new LoginDTO();
        login.setUser(loginUser.getUsername());
        login.setPassword(loginUser.getPassword());

        when(repository.save(captor.capture())).thenAnswer(u -> {
            User user = u.getArgument(0, User.class);
            return user;
        });
        when(repository.findByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword())).thenReturn(loginUser);

        ResultActions post = mockMvc.perform(post("/user/logout").
                contentType(MediaType.APPLICATION_JSON).
                content(mapper.writeValueAsString(login)));

        post.andDo(print())
                .andExpect(status().isOk());

        User user = captor.getValue();
        assertEquals(user.getLogged(), false);
    }

    @Test
    public void shouldNotLogoutNotLoggedUser() throws Exception {
        User loginUser = UserBuilder.defaults().build();
        loginUser.setLogged(false);
        LoginDTO login = new LoginDTO();
        login.setUser(loginUser.getUsername());
        login.setPassword(loginUser.getPassword());

        when(repository.findByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword())).thenReturn(loginUser);

        ResultActions post = mockMvc.perform(post("/user/logout").
                contentType(MediaType.APPLICATION_JSON).
                content(mapper.writeValueAsString(login)));

        post.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotLogoutInvalidUser() throws Exception {
        User loginUser = UserBuilder.defaults().build();

        LoginDTO login = new LoginDTO();
        login.setUser(loginUser.getUsername());
        login.setPassword(loginUser.getPassword());


        ResultActions post = mockMvc.perform(post("/user/logout").
                contentType(MediaType.APPLICATION_JSON).
                content(mapper.writeValueAsString(login)));

        post.andDo(print())
                .andExpect(status().isNotFound());
    }
}
