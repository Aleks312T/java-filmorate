package ru.yandex.practicum.controller;

import lombok.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

@RestController
@RequestMapping("/Film")
public class FilmController {
    private final Map<String, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Map<String, Film> findAll() {
        log.debug("Получен запрос Get /Film.");
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    @PostMapping
    public Film create(@RequestBody @NonNull Film film) {
        log.debug("Получен запрос Post /Film.");
        if(validateFilm(film))
        {

        } else
        {
            throw new InputMismatchException();
        }
        return film;
    }

    @PutMapping
    public Film put(@RequestBody @NonNull Film film) {
        log.debug("Получен запрос Put /Film.");
        if(validateFilm(film))
        {

        } else
        {
            throw new InputMismatchException();
        }
        return film;
    }

    private boolean validateFilm(Film film)
    {
        if(film.getDescription().length() > 200)
            return false;
        //День рождение кино
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            return false;
        if(film.getReleaseDate().isAfter(LocalDate.now()))
            return false;
        if(film.getDuration().isZero())
            return false;

        return true;
    }
}
