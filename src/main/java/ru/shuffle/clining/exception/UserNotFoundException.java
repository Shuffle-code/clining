package ru.shuffle.clining.exception;

public class UserNotFoundException extends IllegalArgumentException {

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
