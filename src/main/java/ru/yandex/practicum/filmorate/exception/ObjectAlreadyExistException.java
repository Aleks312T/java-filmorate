package ru.yandex.practicum.filmorate.exception;

import java.util.InputMismatchException;

public class ObjectAlreadyExistException extends InputMismatchException {
    public ObjectAlreadyExistException() {
        super();
    }

    public ObjectAlreadyExistException(String s) {
        super(s);
    }
}
