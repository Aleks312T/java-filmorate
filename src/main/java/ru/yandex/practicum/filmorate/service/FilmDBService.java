package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NamelessObjectException;
import ru.yandex.practicum.filmorate.storage.dao.film.impl.FilmStorageDBImpl;
import ru.yandex.practicum.filmorate.storage.dao.user.impl.UserStorageDBImpl;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.InputMismatchException;

@RequiredArgsConstructor
@Service
public class FilmDBService {
    private static final Logger log = LoggerFactory.getLogger(FilmDBService.class);

    private final FilmStorageDBImpl filmStorageDB;
    private final UserStorageDBImpl userStorageDB;


    public Collection<Film> findAll() {
        return filmStorageDB.findAll();
    }

    public Film create(Film film) {
        if (film == null) {
            String errorMessage = "Отсутствуют входные данные.";
            validationError(errorMessage, "NullPointerException");
        } else
        if (film.getId() != null && filmStorageDB.getFilm(film.getId()) != null) {
            String errorMessage = "Такой фильм уже есть.";
            validationError(errorMessage, "ObjectAlreadyExistException");
        }
        if (validateFilm(film))
            return filmStorageDB.createFilm(film);
        else {
            String errorMessage = "Фильм не прошел валидацию.";
            validationError(errorMessage, "ValidationException");
        }
        return film;
    }

    public Film put(Film film) {
        if (film == null) {
            String errorMessage = "Отсутствуют входные данные.";
            validationError(errorMessage, "NullPointerException");
        } else
        if (film.getId() == null) {
            String errorMessage = "Отсутствует входной идентификатор.";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else
        if (filmStorageDB.getFilm(film.getId()) == null) {
            String errorMessage = "Фильма с Id " + film.getId() + " нет.";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else
        if (!validateFilm(film)) {
            String errorMessage = "Фильм не прошел валидацию.";
            validationError(errorMessage, "ValidationException");
        } else {
            return filmStorageDB.updateFilm(film);
        }
        return film;
    }

    public Film getFilm(Integer filmId) {
        if (filmId == null) {
            String errorMessage = "Отсутствуют входные данные.";
            validationError(errorMessage, "NullPointerException");
        } else {
            Film result = filmStorageDB.getFilm(filmId);
            if (result == null) {
                String errorMessage = "Фильма с Id " + filmId + " нет.";
                validationError(errorMessage, "ObjectDoesntExistException");
            } else
                return result;
        }
        return getFilm(filmId);
    }

    public Film addLike(int filmId, int userId) {
        if (filmStorageDB.getFilm(filmId) == null) {
            String errorMessage = "Фильма с Id " + filmId + " не существует.";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else
        if (userStorageDB.getUser(userId) == null) {
            String errorMessage = "Пользователя с Id " + userId + " не существует.";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else
            filmStorageDB.removeLike(filmId, userId);
            return filmStorageDB.addLike(filmId, userId);
    }

    public Film removeLike(int filmId, int userId) {
        if (filmStorageDB.getFilm(filmId) == null) {
            String errorMessage = "Фильма с Id " + filmId + " не существует.";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else
        if (userStorageDB.getUser(userId) == null) {
            String errorMessage = "Пользователя с Id " + userId + " не существует.";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else
            return filmStorageDB.removeLike(filmId, userId);
        return getFilm(filmId);
    }

    public Collection<Film> getPopularFilm(int count) {
        if (count < 0) {
            String errorMessage = "Количество фильмов не может быть меньше 0.";
            validationError(errorMessage, "InputMismatchException");
        }
        return filmStorageDB.getPopularFilm(count);
    }

    private boolean validateFilm(@Valid Film film) {
        return (!film.getName().isBlank())
                && (film.getDescription().length() <= 200)
                && (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)))
                && (film.getReleaseDate().isBefore(LocalDate.now()))
                && (film.getDuration() > 0);
    }

    private void validationError(String errorMessage, String exception) {
        log.warn(errorMessage);
        switch (exception) {
            case "ObjectDoesntExistException":
                throw new ObjectDoesntExistException(errorMessage);
            case "ObjectAlreadyExistException":
                throw new ObjectAlreadyExistException(errorMessage);
            case "NullPointerException":
                throw new NullPointerException(errorMessage);
            case "InputMismatchException":
                throw new InputMismatchException(errorMessage);
            case "ValidationException":
                throw new ValidationException(errorMessage);
            case "NamelessObjectException":
                throw new NamelessObjectException(errorMessage);
            default:
                throw new RuntimeException(errorMessage);
        }
    }
}
