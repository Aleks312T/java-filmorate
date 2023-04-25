package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NamelessObjectException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
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

    public Film put(Film film) {
        if (!films.containsKey(film.getId()) || film.getId() == 0) {
            String errorMessage = "На обновление пришел фильм с неизвестным Id = " + film.getId();
            log.warn(errorMessage);

            throw new NamelessObjectException();
        } else
            //Как без вызова такой функции пробрасывать нужные исключения с корректными сообщениями?
            //Я +- понял как работает Valid и все вокруг него, но он видимо просто не допускает переменную, из-за
            //чего не дает анализировать неверные входные данные, выкидывать кастомные исключения и т.д.
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

    private boolean validateFilm(@Valid Film film) {
        return (!film.getName().isBlank())
                && (film.getDescription().length() <= 200)
                && (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)))
                && (film.getReleaseDate().isBefore(LocalDate.now()))
                && (film.getDuration() > 0);
    }
}
