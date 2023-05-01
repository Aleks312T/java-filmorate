package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NamelessObjectException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 0;

    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    public Film create(Film film) {
        if (films.containsKey(film.getId())) {
            String errorMessage = "Фильм с Id " + film.getId() + " уже существует.";
            log.warn(errorMessage);
            throw new ObjectAlreadyExistException();
        } else
        if (validateFilm(film)) {
            log.trace("Фильм {} прошел валидацию", film.getId());
            if (film.getId() ==  null || film.getId() == 0)
                film.setId(++id);
            films.put(film.getId(), film);
        } else {
            String errorMessage = "Фильм " + film.getId() + " не прошел валидацию";
            log.warn(errorMessage);
            throw new ValidationException();
        }
        return film;
    }

    public Film put(Film film) {
        if (!films.containsKey(film.getId()) || film.getId() == 0) {
            String errorMessage = "На обновление пришел фильм с неизвестным Id = " + film.getId();
            log.warn(errorMessage);
            throw new NamelessObjectException();
        } else
            if (validateFilm(film)) {
                log.trace("Фильм {} прошел валидацию", film.getId());
                //Не выводим ошибку о наличии фильма из-за метода put
                films.put(film.getId(), film);
            } else {
                String errorMessage = "Фильм " + film.getId() + " не прошел валидацию";
                log.warn(errorMessage);
                throw new ValidationException();
            }
        return film;
    }

    public boolean containFilm(Film film) {
        log.debug("Вызов функции {} с входными данными {}.",
                "containFilm", film.getId());
        return films.containsValue(film);
    }

    public boolean containFilmId(int id) {
        log.debug("Вызов функции {} с входными данными {}.",
                "containFilmId", id);
        return films.containsKey(id);
    }

    public Film getFilmById(int id) {
        log.debug("Вызов функции {} с входными данными {}.",
                "getFilmById", id);
        return films.getOrDefault(id, null);
    }

    private boolean validateFilm(@Valid Film film) {
        return (!film.getName().isBlank())
                && (film.getDescription().length() <= 200)
                && (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)))
                && (film.getReleaseDate().isBefore(LocalDate.now()))
                && (film.getDuration() > 0);
    }
}
