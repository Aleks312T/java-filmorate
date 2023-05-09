package ru.yandex.practicum.filmorate.exception;

public class ObjectAlreadyExistException extends RuntimeException {
    public ObjectAlreadyExistException() {
        super();
    }

    public ObjectAlreadyExistException(String s) {
        super(s);
    }
}
