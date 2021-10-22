package com.bookshelf.controller;

import com.bookshelf.dto.LoginDTO;
import com.bookshelf.model.User;
import com.bookshelf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user){
        return ResponseEntity.ok(service.create(user));
    }

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody LoginDTO login){
        return ResponseEntity.ok(service.userLogin(login, true).getId());
    }

    @PostMapping(value = "/logout")
    public ResponseEntity logout(@RequestBody LoginDTO login){
        return ResponseEntity.ok(service.userLogin(login, false).getId());
    }
}
