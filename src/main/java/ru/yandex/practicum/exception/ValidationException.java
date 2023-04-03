package ru.yandex.practicum.exception;

import java.util.InputMismatchException;

public class ValidationException extends InputMismatchException {
    public ValidationException() {
        super();
    }

    public ValidationException(String s) {
        super(s);
    }
}
