package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.NamelessObjectException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;

import javax.servlet.ServletException;
import java.sql.SQLException;

@ControllerAdvice
public class ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)                   //404
    public ErrorResponse handleNamelessObjectException(final NamelessObjectException e) {
        String errorMessage = "Пришел безымянный объект.";
        log.warn(errorMessage);
        return new ErrorResponse(errorMessage, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)                   //404
    public ErrorResponse handleServletException(final ServletException e) {
        String errorMessage = "Костыльная обработка ошибки.";
        log.warn(errorMessage);
        return new ErrorResponse(errorMessage, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)                 //400
    public ErrorResponse handleObjectAlreadyExistException(final ObjectAlreadyExistException e) {
        String errorMessage = "Объект уже существует.";
        log.warn(errorMessage);
        return new ErrorResponse(errorMessage, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)                   //404
    public ErrorResponse handleObjectDoesntExistException(final ObjectDoesntExistException e) {
        String errorMessage = "Объекта не существует.";
        log.warn(errorMessage);
        return new ErrorResponse(errorMessage, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)                 //400
    public ErrorResponse handleValidationException(final ValidationException e) {
        String errorMessage = "Объект не прошел проверку.";
        log.warn(errorMessage);
        return new ErrorResponse(errorMessage, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)                 //400
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        String errorMessage = "Некорректные входные аргументы.";
        log.warn(errorMessage);
        return new ErrorResponse(errorMessage, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)       //500
    public ErrorResponse handleSQLException(final SQLException e) {
        String errorMessage = "Произошла ошибка в базе данных.";
        log.warn(errorMessage);
        return new ErrorResponse(errorMessage, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)       //500
    public ErrorResponse handleUnknownException(final Throwable e) {
        String errorMessage = "Произошла непредвиденная ошибка.";
        log.warn(errorMessage);
        return new ErrorResponse(errorMessage, e.getMessage());
    }
}