package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.dao.impl.FilmStorageDBImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserStorageDBImpl;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.stream.Collectors;

@Service
public class FilmDBService {
    private static final Logger log = LoggerFactory.getLogger(FilmDBService.class);

    //TODO: в конце поменять обратно
    private final FilmStorageDBImpl filmStorageDB;
    private final UserStorageDBImpl userStorageDB;

    @Autowired
    public FilmDBService(FilmStorageDBImpl filmStorage, UserStorageDBImpl userStorage) {
        this.filmStorageDB = filmStorage;
        this.userStorageDB = userStorage;
    }

    public Collection<Film> findAll() {
        return filmStorageDB.findAll();
    }

    public Film create(Film film) {
        if (film == null) {
            String errorMessage = "Отсутствуют входные данные.";
            log.warn(errorMessage);
            throw new NullPointerException(errorMessage);
        } else
        if (film.getId() != null && filmStorageDB.getFilm(film.getId()) != null) {
            String errorMessage = "Такой фильм уже есть.";
            log.warn(errorMessage);
            throw new ObjectAlreadyExistException(errorMessage);
        }
        if(validateFilm(film))
            return filmStorageDB.createFilm(film);
        else {
            String errorMessage = "Фильм не прошел валидацию.";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }

    public Film put(Film film) {
        if (film == null) {
            String errorMessage = "Отсутствуют входные данные.";
            log.warn(errorMessage);
            throw new NullPointerException(errorMessage);
        } else
        if(film.getId() == null) {
            String errorMessage = "Отсутствует входной идентификатор.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
        if (filmStorageDB.getFilm(film.getId()) == null) {
            String errorMessage = "Фильма с Id " + film.getId() + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
        if(!validateFilm(film)) {
            String errorMessage = "Фильм не прошел валидацию.";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        } else {
            return filmStorageDB.updateFilm(film);
        }
    }

    public Film getFilm(Integer filmId) {

        if (filmId == null) {
            String errorMessage = "Отсутствуют входные данные.";
            log.warn(errorMessage);
            throw new NullPointerException(errorMessage);
        } else
        {
            Film result = filmStorageDB.getFilm(filmId);
            if (result == null) {
                String errorMessage = "Фильма с Id " + filmId + " нет.";
                log.warn(errorMessage);
                throw new ObjectDoesntExistException(errorMessage);
            } else
                return result;
        }

    }

    public Film addLike(int filmId, int userId) {
        if (filmStorageDB.getFilm(filmId) == null) {
            String errorMessage = "Фильма с Id " + filmId + " не существует.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
        if (userStorageDB.getUser(userId) == null) {
            String errorMessage = "Пользователя с Id " + userId + " не существует.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
            filmStorageDB.removeLike(filmId, userId);
            return filmStorageDB.addLike(filmId, userId);
    }

    public Film removeLike(int filmId, int userId) {
        if (filmStorageDB.getFilm(filmId) == null) {
            String errorMessage = "Фильма с Id " + filmId + " не существует.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
        if (userStorageDB.getUser(userId) == null) {
            String errorMessage = "Пользователя с Id " + userId + " не существует.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
            return filmStorageDB.removeLike(filmId, userId);
    }

    public Collection<Film> getPopularFilm(int count) {
        if (count < 0) {
            String errorMessage = "Количество фильмов не может быть меньше 0.";
            log.warn(errorMessage);
            throw new InputMismatchException(errorMessage);
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
}
