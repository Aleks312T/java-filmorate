package ru.yandex.practicum.filmorate.exception;

//public class ObjectDoesntExistException extends InputMismatchException {
public class ObjectDoesntExistException extends RuntimeException {
    public ObjectDoesntExistException() {
        super();
    }

    public ObjectDoesntExistException(String s) {
        super(s);
    }
}
