package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос Get /films.");
        return filmService.findAll();
    }

    @PostMapping
    @Validated
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос Post /films.");
        return filmService.create(film);
    }

    @PutMapping
    @Validated
    public Film put(@Valid @RequestBody Film film) {
        log.info("Получен запрос Put /films.");
        return filmService.put(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@Valid @RequestBody @PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.info("Получен запрос Put /{}/like/{}.", filmId, userId);
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@Valid @RequestBody @PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.info("Получен запрос DeleteMapping /{}/like/{}.", filmId, userId);
        return filmService.removeLike(filmId, userId);
    }

    //По аналогии с возвращением списка друзей
    @GetMapping("/{id}/likes")
    public Collection<Integer> getLikes(@Valid @RequestBody @PathVariable("id") int filmId) {
        log.info("Получен запрос Get /{}/likes.", filmId);
        return filmService.returnLikes(filmId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilm(@Valid @RequestBody @RequestParam(defaultValue = "10", required = false)
                                               Integer count) {
        log.info("Получен запрос DeleteMapping /popular с параметром count = {} .", count);
        return filmService.getPopularFilm(count);
    }

    @GetMapping("/{id}")
    public Film getFilm(@Valid @RequestBody @PathVariable("id") int filmId) {
        log.info("Получен запрос DeleteMapping /{}.", filmId);
        return filmService.getFilm(filmId);
    }

}
