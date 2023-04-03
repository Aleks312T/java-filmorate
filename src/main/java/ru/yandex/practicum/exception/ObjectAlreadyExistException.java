package ru.yandex.practicum.exception;

import java.util.InputMismatchException;

public class ObjectAlreadyExistException extends InputMismatchException {
    public ObjectAlreadyExistException() {
        super();
    }

    public ObjectAlreadyExistException(String s) {
        super(s);
    }
}
