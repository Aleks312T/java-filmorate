package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NamelessObjectException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private Integer id = 0;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос Get /films.");
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    @Validated
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос Post /films.");
        if (films.containsKey(film.getId())) {
            String errorMessage = "Фильм с Id " + film.getId() + " уже существует.";
            log.warn(errorMessage);
            throw new ObjectAlreadyExistException();
        } else
        if (validateFilm(film)) {
            log.trace("Фильм {} прошел валидацию", film.getId());
            if (film.getId() == 0)
                film.setId(++id);
            films.put(film.getId(), film);
        } else {
            String errorMessage = "Фильм " + film.getId() + " не прошел валидацию";
            log.warn(errorMessage);
            throw new ValidationException();
        }
        return film;
    }

    @PutMapping
    @Validated
    public Film put(@Valid @RequestBody Film film) {
        log.info("Получен запрос Put /films.");
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

    @Validated
    private boolean validateFilm(@Valid Film film) {
        if (film.getName().isBlank())
            return false;
        if (film.getDescription().length() > 200)
            return false;
        //День рождение кино
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            return false;
        if (film.getReleaseDate().isAfter(LocalDate.now()))
            return false;
        if (film.getDuration() <= 0)
            return false;

        return true;
    }
}
