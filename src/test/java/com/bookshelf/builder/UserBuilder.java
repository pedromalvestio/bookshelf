package com.bookshelf.builder;

import com.bookshelf.model.Book;
import com.bookshelf.model.User;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;

public class UserBuilder {

    public static GenericBuilder<User> defaults() {
        return GenericBuilder.of(() -> createObjectDefault());
    }

    private static User createObjectDefault() {
        User builder = new User();
        builder.setUsername(randomAlphanumeric(20));
        builder.setPassword(randomAlphanumeric(10));
        builder.setLogged(true);
        builder.setId(nextLong(1,1000));
        return builder;
    }
}
