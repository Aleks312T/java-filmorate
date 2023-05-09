package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmControllerTest {
    static FilmController filmController;
    static FilmService customFilmService;

    @BeforeEach
    void beforeEach() {
        customFilmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        User user1 = new User("login1",
                "qwerty1@mail.ru",
                LocalDate.of(2001, 10, 10));
        user1.setId(1);
        User user2 = new User("login2",
                "qwerty2@mail.ru",
                LocalDate.of(2002, 10, 10));
        user2.setId(2);
        User user3 = new User("login3",
                "qwerty3@mail.ru",
                LocalDate.of(2003, 10, 10));
        user3.setId(3);
        customFilmService.userStorage.create(user1);
        customFilmService.userStorage.create(user2);
        customFilmService.userStorage.create(user3);
        filmController = new FilmController(customFilmService);
    }

    @Test
    void shouldPostFilmsOK() {
        Film film = Film.builder()
                .name("qwerty")
                .description("Description")
                .releaseDate(LocalDate.of(1999, 10, 10))
                .duration(120)
                .build();
        assertEquals(film, filmController.create(film));
    }

    @Test
    void shouldPutAndCreateSameFilmOK() {
        Film film = Film.builder()
                .name("qwerty")
                .description("Description")
                .releaseDate(LocalDate.of(1999, 10, 10))
                .duration(120)
                .build();
        assertEquals(film, filmController.create(film));
        assertEquals(film, filmController.put(film));
    }

    @Test
    void shouldGetAllFilmsOK() {
        Film film1 = Film.builder()
                .name("qwerty1")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        Film film2 = Film.builder()
                .name("qwerty2")
                .description("Description")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(180)
                .id(2)
                .build();
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
    void shouldAddLikesOK() {
        Film film1 = Film.builder()
                .name("qwerty1")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        Film film2 = Film.builder()
                .name("qwerty2")
                .description("Description")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(180)
                .id(2)
                .build();
        assertEquals(film1, filmController.create(film1));
        assertEquals(film2, filmController.create(film2));

        filmController.addLike(1, 1);
        filmController.addLike(1, 2);
        filmController.addLike(1, 3);

        filmController.addLike(2, 1);
        filmController.addLike(2, 3);

        HashSet<Integer> result1 = new HashSet<>();
        result1.add(1);
        result1.add(2);
        result1.add(3);
        HashSet<Integer> result2 = new HashSet<>();
        result2.add(1);
        result2.add(3);

        assertEquals(result1, filmController.getLikes(1));
        assertEquals(result2, filmController.getLikes(2));
    }

    @Test
    void shouldAddAndRemoveLikesOK() {
        Film film1 = Film.builder()
                .name("qwerty1")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        assertEquals(film1, filmController.create(film1));

        filmController.addLike(1, 1);
        filmController.addLike(1, 2);
        filmController.addLike(1, 3);

        HashSet<Integer> result1 = new HashSet<>();
        result1.add(1);
        result1.add(2);
        result1.add(3);
        assertEquals(result1, filmController.getLikes(1));

        HashSet<Integer> result2 = new HashSet<>();
        result2.add(1);
        result2.add(3);
        filmController.deleteLike(1, 2);
        assertEquals(result2, filmController.getLikes(1));

        filmController.deleteLike(1, 1);
        filmController.deleteLike(1, 3);
        assertEquals(new HashSet<>(), filmController.getLikes(1));
    }

    @Test
    void shouldGetPopularFilmOK() {
        Film film1 = Film.builder()
                .name("qwerty1")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        Film film2 = Film.builder()
                .name("qwerty2")
                .description("Description")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(180)
                .id(2)
                .build();
        Film film3 = Film.builder()
                .name("qwerty3")
                .description("Description")
                .releaseDate(LocalDate.of(2003, 3, 3))
                .duration(120)
                .id(3)
                .build();
        Film film4 = Film.builder()
                .name("qwerty4")
                .description("Description")
                .releaseDate(LocalDate.of(2004, 4, 4))
                .duration(140)
                .id(4)
                .build();
        Film film5 = Film.builder()
                .name("qwerty5")
                .description("Description")
                .releaseDate(LocalDate.of(2005, 5, 5))
                .duration(50)
                .id(5)
                .build();
        assertEquals(film1, filmController.create(film1));
        assertEquals(film2, filmController.create(film2));
        assertEquals(film3, filmController.create(film3));
        assertEquals(film4, filmController.create(film4));
        assertEquals(film5, filmController.create(film5));

        HashSet<Film> result1 = new HashSet<>();
        result1.add(film1);
        result1.add(film2);
        assertEquals(result1, filmController.getPopularFilm(2));
        result1.add(film3);
        result1.add(film4);
        assertEquals(result1, filmController.getPopularFilm(4));


        filmController.addLike(1, 1);
        filmController.addLike(1, 2);
        filmController.addLike(1, 3);

        filmController.addLike(2, 2);

        filmController.addLike(3, 1);
        filmController.addLike(3, 2);
        filmController.addLike(3, 3);

        filmController.addLike(4, 1);
        filmController.addLike(4, 2);

        HashSet<Film> result2 = new HashSet<>();

        result2.add(film1);
        assertEquals(result2, filmController.getPopularFilm(1));

        result2.add(film3);
        assertEquals(result2, filmController.getPopularFilm(2));

        result2.add(film4);
        assertEquals(result2, filmController.getPopularFilm(3));

        result2.add(film2);
        assertEquals(result2, filmController.getPopularFilm(4));

        result2.add(film5);
        assertEquals(result2, filmController.getPopularFilm(5));

        assertEquals(result2, filmController.getPopularFilm(6));
        assertEquals(result2, filmController.getPopularFilm(7));
        //Жаль, что нельзя в тесте проверить без входного параметра
        assertEquals(result2, filmController.getPopularFilm(15));
    }

    @Test
    void shouldNotAddLikesFromUnknownUser() {
        Film film1 = Film.builder()
                .name("qwerty1")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        assertEquals(film1, filmController.create(film1));

        filmController.addLike(1, 1);
        Assertions.assertThrows(ObjectDoesntExistException.class, () -> {
            filmController.addLike(1, 11111);
        });
    }

    @Test
    void shouldNotRemoveLikesFromUnknownUser() {
        Film film1 = Film.builder()
                .name("qwerty1")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        assertEquals(film1, filmController.create(film1));

        filmController.addLike(1, 1);
        filmController.addLike(1, 2);
        filmController.addLike(1, 3);
        Assertions.assertThrows(ObjectDoesntExistException.class, () -> {
            filmController.deleteLike(1, 11111);
        });
    }

    @Test
    void shouldNotAddLikesToUnknownFilm() {
        Film film1 = Film.builder()
                .name("qwerty1")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        assertEquals(film1, filmController.create(film1));

        filmController.addLike(1, 1);
        Assertions.assertThrows(ObjectDoesntExistException.class, () -> {
            filmController.addLike(111111, 1);
        });
    }

    @Test
    void shouldNotRemoveLikesFromUnknownFilm() {
        Film film1 = Film.builder()
                .name("qwerty1")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        assertEquals(film1, filmController.create(film1));

        filmController.addLike(1, 1);
        Assertions.assertThrows(ObjectDoesntExistException.class, () -> {
            filmController.deleteLike(111111, 1);
        });
    }

    @Test
    void shouldGetAllFilmsAfterChangesOK() {
        Film film1 = Film.builder()
                .name("qwerty1")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        Film film2 = Film.builder()
                .name("qwerty2")
                .description("Description")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(180)
                .id(2)
                .build();

        Film newFilm1 = Film.builder()
                .name("qwerty1")
                .description("Description changed")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(110)
                .id(1)
                .build();

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
        Film film1 = Film.builder()
                .name("qwerty")
                .description("Description1")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        Film film2 = Film.builder()
                .name("qwerty")
                .description("Description2")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(180)
                .id(1)
                .build();
        assertEquals(film1, filmController.create(film1));
        assertEquals(film2, filmController.put(film2));
    }

    @Test
    void shouldNotPutUnknownFilm() {
        Film film = Film.builder()
                .name("qwerty")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
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
        Film film1 = Film.builder()
                .name("qwerty1")
                .description("Description1")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        Film film2 = Film.builder()
                .name("qwerty2")
                .description("Description2")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(180)
                .id(1)                  //Ошибочное ID
                .build();

        filmController.create(film1);
        Assertions.assertThrows(ObjectAlreadyExistException.class, () -> {
            filmController.create(film2);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfName() throws ValidationException {
        Film film = Film.builder()
                .name("          ")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDateInPast() throws ValidationException {
        Film film = Film.builder()
                .name("qwerty1")
                .description("Description")
                .releaseDate(LocalDate.of(9, 11, 11))
                .duration(120)
                .id(1)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDateInFuture() throws ValidationException {
        Film film = Film.builder()
                .name("qwerty")
                .description("Description")
                .releaseDate(LocalDate.of(200001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDescription() throws ValidationException {
        //Очень длинное описание...
        Film film = Film.builder()
                .name("qwerty1")
                .description("1".repeat(201))
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .id(1)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDuration() throws ValidationException {
        Film film = Film.builder()
                .name("qwerty1")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(-120)
                .id(1)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }
}
