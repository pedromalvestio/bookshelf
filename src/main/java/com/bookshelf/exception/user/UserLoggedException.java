package com.bookshelf.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User is already in this login status")
public class UserLoggedException extends RuntimeException{
}
