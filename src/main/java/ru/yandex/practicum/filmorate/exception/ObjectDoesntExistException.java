package ru.yandex.practicum.filmorate.exception;

import java.util.InputMismatchException;

public class ObjectDoesntExistException extends InputMismatchException {
    public ObjectDoesntExistException() {
        super();
    }

    public ObjectDoesntExistException(String s) {
        super(s);
    }
}
