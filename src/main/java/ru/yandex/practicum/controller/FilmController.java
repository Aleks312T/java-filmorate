package ru.yandex.practicum.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.util.*;

@RestController
@RequestMapping("/Film")
public class FilmController {
    private final ArrayList<Film> films = new ArrayList<>();

    @GetMapping
    public ArrayList<Film> findAll() {
        return films;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {

        return film;
    }
}
