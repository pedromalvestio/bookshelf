package com.bookshelf.service;

import com.bookshelf.dto.LoginDTO;
import com.bookshelf.exception.user.UserLoggedException;
import com.bookshelf.exception.user.UserNotFoundException;
import com.bookshelf.model.User;
import com.bookshelf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User create(User user){
        return repository.save(user);
    }

    public User userLogin(LoginDTO login, boolean status) {
        User user = repository.findByUsernameAndPassword(login.getUser(), login.getPassword());
        if (user == null) throw new UserNotFoundException();
        if (user.getLogged() == status) throw new UserLoggedException();
        user.setLogged(status);
        return repository.save(user);
    }

    public User userLogged(Long userId){
        User user = repository.findById(userId).get();
        if (!user.getLogged()) throw new UserLoggedException();
        return user;
    }
}
