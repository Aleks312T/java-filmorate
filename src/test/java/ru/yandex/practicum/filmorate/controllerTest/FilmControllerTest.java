package ru.yandex.practicum.filmorate.controllerTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {

    private final FilmController filmController;
    private Validator validator;
    Film film1 = Film.builder()
            .name("qwerty1")
            .description("Description")
            .releaseDate(LocalDate.of(2001, 11, 11))
            .duration(120)
            .mpa(new Mpa(1,"G"))
            .id(1)
            .build();
    Film film2 = Film.builder()
            .name("qwerty2")
            .description("Description")
            .releaseDate(LocalDate.of(2002, 12, 12))
            .duration(180)
            .mpa(new Mpa(1,"G"))
            .id(2)
            .build();
    Film film3 = Film.builder()
            .name("qwerty3")
            .description("Description")
            .releaseDate(LocalDate.of(2003, 3, 3))
            .duration(120)
            .mpa(new Mpa(1,"G"))
            .id(3)
            .build();
    Film film4 = Film.builder()
            .name("qwerty4")
            .description("Description")
            .releaseDate(LocalDate.of(2004, 4, 4))
            .duration(140)
            .mpa(new Mpa(1,"G"))
            .id(4)
            .build();
    Film film5 = Film.builder()
            .name("qwerty5")
            .description("Description")
            .releaseDate(LocalDate.of(2005, 5, 5))
            .duration(50)
            .mpa(new Mpa(1,"G"))
            .id(5)
            .build();

    @BeforeEach
    void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldNotAddLikesFromUnknownUser() {
        Assertions.assertThrows(ObjectDoesntExistException.class, () -> {
            filmController.addLike(1, 11111);
        });
    }

    @Test
    void shouldNotRemoveLikesFromUnknownUser() {
        Assertions.assertThrows(ObjectDoesntExistException.class, () -> {
            filmController.deleteLike(1, 11111);
        });
    }

    @Test
    void shouldNotAddLikesToUnknownFilm() {
        Assertions.assertThrows(ObjectDoesntExistException.class, () -> {
            filmController.addLike(111111, 1);
        });
    }

    @Test
    void shouldNotRemoveLikesFromUnknownFilm() {
        Assertions.assertThrows(ObjectDoesntExistException.class, () -> {
            filmController.deleteLike(111111, 1);
        });
    }

    @Test
    void shouldNotPutUnknownFilm() {
        Film film1131 = Film.builder()
                .name("qwerty")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .mpa(new Mpa(1,"G"))
                .id(1131313)
                .build();
        Assertions.assertThrows(RuntimeException.class, () -> {
            filmController.put(film1131);
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
    void shouldThrowValidationExceptionBecauseOfName() {
        Film film13 = Film.builder()
                .name("          ")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .mpa(new Mpa(1,"G"))
                .id(13)
                .build();
        Assertions.assertThrows(Exception.class, () -> {
            filmController.create(film13);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDateInPast() {
        Film film414 = Film.builder()
                .name("qwerty1")
                .description("Description")
                .releaseDate(LocalDate.of(9, 11, 11))
                .duration(120)
                .mpa(new Mpa(1,"G"))
                .id(414)
                .build();
        Assertions.assertThrows(Exception.class, () -> {
            filmController.create(film414);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDateInFuture() {
        Film film560 = Film.builder()
                .name("qwerty")
                .description("Description")
                .releaseDate(LocalDate.of(200001, 11, 11))
                .duration(120)
                .mpa(new Mpa(1,"G"))
                .id(560)
                .build();
        Assertions.assertThrows(Exception.class, () -> {
            filmController.create(film560);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDescription() {
        //Очень длинное описание...
        Film film33333 = Film.builder()
                .name("qwerty1")
                .description("1".repeat(201))
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(120)
                .mpa(new Mpa(1,"G"))
                .id(33333)
                .build();
        Assertions.assertThrows(Exception.class, () -> {
            filmController.create(film33333);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDuration() {
        Film film4785 = Film.builder()
                .name("qwerty1")
                .description("Description")
                .releaseDate(LocalDate.of(2001, 11, 11))
                .duration(-120)
                .mpa(new Mpa(1,"G"))
                .id(4785)
                .build();
        Assertions.assertThrows(Exception.class, () -> {
            filmController.create(film4785);
        });
    }
}
