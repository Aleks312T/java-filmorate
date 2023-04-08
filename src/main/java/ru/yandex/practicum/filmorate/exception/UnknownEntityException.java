package ru.yandex.practicum.filmorate.exception;

import java.util.InputMismatchException;

public class UnknownEntityException extends InputMismatchException {
    public UnknownEntityException() {
        super();
    }

    public UnknownEntityException(String s) {
        super(s);
    }
}
