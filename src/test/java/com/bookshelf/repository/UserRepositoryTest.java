package com.bookshelf.repository;

import com.bookshelf.builder.UserBuilder;
import com.bookshelf.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@TestPropertySource("file:src/test/resources/test.properties")
@DataJpaTest
@Sql("file:src/test/resources/database.sql")
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void shouldSaveAnUser(){
        User user = repository.saveAndFlush(UserBuilder.defaults().build());
        entityManager.detach(user);
        User findUser = repository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        assertEquals(user.getId(), findUser.getId());
        assertEquals(user.getUsername(), findUser.getUsername());
        assertEquals(user.getPassword(), findUser.getPassword());
    }
}
