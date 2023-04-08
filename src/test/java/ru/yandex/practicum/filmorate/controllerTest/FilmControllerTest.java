package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmControllerTest {
    static FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void shouldPostFilmsOK() {
        Film film = new Film("qwerty",
                "Description",
                LocalDate.of(1999, 10, 10),
                120);
        assertEquals(film, filmController.create(film));
    }

    @Test
    void shouldPutAndCreateSameFilmOK() {
        Film film = new Film("qwerty",
                "Description",
                LocalDate.of(1999, 10, 10),
                120);
        assertEquals(film, filmController.create(film));
        assertEquals(film, filmController.put(film));
    }

    @Test
    void shouldGetAllFilmsOK() {
        Film film1 = new Film("qwerty1",
                "Description",
                LocalDate.of(1999, 10, 10),
                120);
        film1.setId(1);
        Film film2 = new Film("qwerty2",
                "Description",
                LocalDate.of(2000, 11, 11),
                180);
        film2.setId(2);
        assertEquals(film1, filmController.create(film1));
        assertEquals(film2, filmController.create(film2));

        Map<Integer, Film> films = new HashMap<>();
        films.put(film1.getId(), film1);
        films.put(film2.getId(), film2);

        //Надо проверять все фильмы, т.к. сравнение assertEquals не работает с мапами
        Collection<Film> result = filmController.findAll();
        for (Film currentFilm: result) {
            Integer currentId = currentFilm.getId();
            assertTrue(films.containsKey(currentId));
            assertEquals(films.get(currentId), currentFilm);
        }
    }

    @Test
    void shouldGetAllFilmsAfterChangesOK() {
        Film film1 = new Film("qwerty1",
                "Description",
                LocalDate.of(1999, 10, 10),
                120);
        film1.setId(1);

        Film film2 = new Film("qwerty2",
                "Description",
                LocalDate.of(2000, 11, 11),
                180);
        film2.setId(2);

        Film newFilm1 = new Film("qwerty1",
                "Description",
                LocalDate.of(2020, 5, 5),
                120);
        newFilm1.setId(1);

        assertEquals(film1, filmController.create(film1));
        assertEquals(film2, filmController.create(film2));
        assertEquals(newFilm1, filmController.put(newFilm1));

        Map<Integer, Film> films = new HashMap<>();
        films.put(film1.getId(), newFilm1);
        films.put(film2.getId(), film2);

        //Надо проверять все фильмы, т.к. сравнение assertEquals не работает с мапами
        Collection<Film> result = filmController.findAll();
        for (Film currentFilm: result) {
            Integer currentId = currentFilm.getId();
            assertTrue(films.containsKey(currentId));
            assertEquals(films.get(currentId), currentFilm);
        }
    }

    @Test
    void shouldPutFilmOKWithChanges() {
        Film film1 = new Film("qwerty",
                "Description1",
                LocalDate.of(1999, 10, 10),
                120);
        film1.setId(1);
        Film film2 = new Film("qwerty",
                "Description2",
                LocalDate.of(2000, 11, 11),
                180);
        film2.setId(1);
        assertEquals(film1, filmController.create(film1));
        assertEquals(film2, filmController.put(film2));
    }

    @Test
    void shouldNotPutUnknownFilm() {
        Film film = new Film("qwerty",
                "Description",
                LocalDate.of(1999, 10, 10),
                120);
        Assertions.assertThrows(RuntimeException.class, () -> {
            filmController.put(film);
        });
    }

    @Test
    void shouldThrowNullPointerExceptionBecauseOfNull() throws NullPointerException {
        Film film = null;
        Assertions.assertThrows(NullPointerException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void shouldThrowObjectAlreadyExistExceptionBecauseOfId() throws ObjectAlreadyExistException {
        Film film1 = new Film("qwerty1",
                "Description1",
                LocalDate.of(1999, 10, 10),
                120);
        film1.setId(1);

        Film film2 = new Film("qwerty2",
                "Description2",
                LocalDate.of(2000, 11, 11),
                180);
        film2.setId(1);

        filmController.create(film1);
        Assertions.assertThrows(ObjectAlreadyExistException.class, () -> {
            filmController.create(film2);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfName() throws ValidationException {
        Film film = new Film("     ",
                "Description",
                LocalDate.of(1999, 10, 10),
                120);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDateInPast() throws ValidationException {
        Film film = new Film("qwerty",
                "Description",
                LocalDate.of(199, 10, 10),
                120);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDateInFuture() throws ValidationException {
        Film film = new Film("qwerty",
                "Description",
                LocalDate.of(19999, 10, 10),
                120);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDescription() throws ValidationException {
        //Очень длинное описание...
        Film film = new Film("qwerty",
                "1".repeat(201),
                LocalDate.of(1999, 10, 10),
                120);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDuration() throws ValidationException {
        Film film = new Film("qwerty",
                "Description",
                LocalDate.of(1999, 10, 10),
                -120);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }
}
