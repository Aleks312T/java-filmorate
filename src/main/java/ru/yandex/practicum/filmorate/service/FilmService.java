package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film put(Film film) {
        return filmStorage.put(film);
    }

    public Film getFilm(int filmId) {
        Film result = filmStorage.getFilmById(filmId);
        if(result == null) {
            String errorMessage = "Фильма с Id " + filmId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException();
        } else
            return result;
    }

    //TODO: сделать функцию addLike
    public Film addLike(int filmId, int userId) {

        return null;
    }

    //TODO: сделать функцию removeLike
    public Film removeLike(int filmId, int userId) {

        return null;
    }

    //TODO: сделать функцию returnLikes
    public Film returnLikes(int filmId) {

        return null;
    }
}
