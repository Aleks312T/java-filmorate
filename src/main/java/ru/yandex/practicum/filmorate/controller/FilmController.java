package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.impl.FilmStorageDBImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserStorageDBImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDBService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    @NonNull
    private final FilmDBService filmService;
//    private final FilmDBService filmService = new FilmDBService(
//            new FilmStorageDBImpl(new JdbcTemplate()),
//            new UserStorageDBImpl(new JdbcTemplate()));

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

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") int filmId) {
        log.info("Получен запрос DeleteMapping /{}.", filmId);
        return filmService.getFilm(filmId);
    }

    @PutMapping
    @Validated
    public Film put(@Valid @RequestBody Film film) {
        log.info("Получен запрос Put /films.");
        return filmService.put(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.info("Получен запрос Put /{}/like/{}.", filmId, userId);
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.info("Получен запрос DeleteMapping /{}/like/{}.", filmId, userId);
        return filmService.removeLike(filmId, userId);
    }

    //По аналогии с возвращением списка друзей
    @GetMapping("/{id}/likes")
    public Collection<Integer> getLikes(@PathVariable("id") int filmId) {
        log.info("Получен запрос Get /{}/likes.", filmId);
        return filmService.returnLikes(filmId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilm(@RequestParam(defaultValue = "10")
                                               Integer count) {
        log.info("Получен запрос DeleteMapping /popular с параметром count = {} .", count);
        return filmService.getPopularFilm(count);
    }

}
