package ru.yandex.practicum.filmorate.exception;

import java.util.InputMismatchException;

//Назвал Entity для гибкого применения исключения
public class NamelessObjectException extends InputMismatchException {
    public NamelessObjectException() {
        super();
    }

    public NamelessObjectException(String s) {
        super(s);
    }
}
