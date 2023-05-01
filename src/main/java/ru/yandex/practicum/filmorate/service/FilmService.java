package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    //Приходится делать публичным чтобы можно было добавить пользователей внутрь.
    public UserStorage userStorage;

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
        if (result == null) {
            String errorMessage = "Фильма с Id " + filmId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException();
        } else
            return result;
    }

    public Film addLike(int filmId, int userId) {
        if (!filmStorage.containFilmId(filmId)) {
            String errorMessage = "Фильма с Id " + filmId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException();
        } else
        if (!userStorage.containUserId(userId)) {
            String errorMessage = "Пользователя с Id " + userId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException();
        } else {
            Film film = filmStorage.findAll().stream()
                    .filter(currentFilm -> currentFilm.getId().equals(filmId))
                    .findFirst().get();
            if (film.getLikes().stream().noneMatch(usersId -> usersId.equals(userId))) {
                film.getLikes().add(userId);
                return film;
            } else {
                String errorMessage = "Пользователь с Id " + userId + " уже поставил лайк фильму с Id " + filmId + ".";
                log.warn(errorMessage);
                throw new ObjectDoesntExistException();
            }
        }
    }

    public Film removeLike(int filmId, int userId) {
        if (!filmStorage.containFilmId(filmId)) {
            String errorMessage = "Фильма с Id " + filmId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException();
        } else
        if (!userStorage.containUserId(userId)) {
            String errorMessage = "Пользователя с Id " + userId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException();
        } else {
            Film film = filmStorage.findAll().stream()
                    .filter(currentFilm -> currentFilm.getId().equals(filmId))
                    .findFirst().get();
            if (film.getLikes().stream().noneMatch(usersId -> usersId.equals(userId))) {
                String errorMessage = "Пользователь с Id " + userId + " не ставил лайк фильму с Id " + filmId + ".";
                log.warn(errorMessage);
                throw new ObjectDoesntExistException();
            } else {
                film.getLikes().remove(userId);
                return film;
            }
        }
    }

    public Collection<Film> getPopularFilm(int count) {
        return filmStorage.findAll().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toSet());
    }

    public Collection<Integer> returnLikes(int filmId) {
        if (!filmStorage.containFilmId(filmId)) {
            String errorMessage = "Фильма с Id " + filmId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException();
        } else {
            Film film = filmStorage.getFilmById(filmId);

            return film.getLikes();
        }
    }

}
