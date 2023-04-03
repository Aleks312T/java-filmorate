package ru.yandex.practicum.filmorate.controllerTest;

import org.assertj.core.internal.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.controller.FilmController;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmControllerTest {
    static FilmController filmController;

    @BeforeEach
    void beforeEach()
    {
        filmController = new FilmController();
    }

    @Test
    void shouldPostFilmsOK() {
        Film film = new Film(1,
                "qwerty",
                LocalDate.of(1999, 10, 10),
                Duration.ofHours(2));
        assertEquals(film, filmController.create(film));
    }

    @Test
    void shouldPutFilmsOK() {
        Film film = new Film(1,
                "qwerty",
                LocalDate.of(1999, 10, 10),
                Duration.ofHours(2));
        assertEquals(film, filmController.put(film));
    }

    @Test
    void shouldPutAndCreateSameFilmOK(){
        Film film = new Film(1,
                "qwerty",
                LocalDate.of(1999, 10, 10),
                Duration.ofHours(2));
        assertEquals(film, filmController.create(film));
        assertEquals(film, filmController.put(film));
    }

    @Test
    void shouldGetAllFilmsOK(){
        Film film1 = new Film(1,
                "qwerty1",
                LocalDate.of(1999, 10, 10),
                Duration.ofHours(2));
        Film film2 = new Film(1,
                "qwerty2",
                LocalDate.of(2000, 11, 11),
                Duration.ofHours(3));
        assertEquals(film1, filmController.create(film1));
        assertEquals(film2, filmController.create(film2));

        Map<String, Film> films = new HashMap<>();
        films.put(film1.getName(), film1);
        films.put(film2.getName(), film2);

        //Надо проверять все фильмы, т.к. сравнение assertEquals не работает с мапами
        Collection<Film> result = filmController.findAll();
        for(Film currentFilm: result)
        {
            String currentName = currentFilm.getName();
            assertTrue(films.containsKey(currentName));
            assertEquals(films.get(currentName), currentFilm);
        }
    }

    @Test
    void shouldGetAllFilmsAfterChangesOK(){
        Film film1 = new Film(1,
                "qwerty1",
                LocalDate.of(1999, 10, 10),
                Duration.ofHours(2));
        Film film2 = new Film(1,
                "qwerty2",
                LocalDate.of(2000, 11, 11),
                Duration.ofHours(3));
        Film newFilm1 = new Film(1,
                "qwerty1",
                LocalDate.of(2020, 5, 5),
                Duration.ofHours(2));
        assertEquals(film1, filmController.create(film1));
        assertEquals(film2, filmController.create(film2));
        assertEquals(newFilm1, filmController.put(newFilm1));

        Map<String, Film> films = new HashMap<>();
        films.put(film1.getName(), newFilm1);
        films.put(film2.getName(), film2);

        //Надо проверять все фильмы, т.к. сравнение assertEquals не работает с мапами
        Collection<Film> result = filmController.findAll();
        for(Film currentFilm: result)
        {
            String currentName = currentFilm.getName();
            assertTrue(films.containsKey(currentName));
            assertEquals(films.get(currentName), currentFilm);
        }
    }

    @Test
    void shouldPutFilmOKWithChanges(){
        Film film1 = new Film(1,
                "qwerty1",
                LocalDate.of(1999, 10, 10),
                Duration.ofHours(2));
        Film film2 = new Film(1,
                "qwerty2",
                LocalDate.of(2000, 11, 11),
                Duration.ofHours(3));
        assertEquals(film1, filmController.create(film1));
        assertEquals(film2, filmController.put(film2));
    }

    @Test
    void shouldThrowNullPointerExceptionBecauseOfNull1() throws NullPointerException{
        Film film = null;
        Assertions.assertThrows(NullPointerException.class, () -> {
            filmController.put(film);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfName() throws ValidationException{
        Film film = new Film(1,
                "     ",
                LocalDate.of(1999, 10, 10),
                Duration.ofHours(2));
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.put(film);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDateInPast() throws ValidationException{
        Film film = new Film(1,
                "     ",
                LocalDate.of(199, 10, 10),
                Duration.ofHours(2));
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.put(film);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDateInFuture() throws ValidationException{
        Film film = new Film(1,
                "     ",
                LocalDate.of(19999, 10, 10),
                Duration.ofHours(2));
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.put(film);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfDescription() throws ValidationException{
        Film film = new Film(1,
                "qwerty",
                LocalDate.of(1999, 10, 10),
                Duration.ofHours(2));
        //Очень длинное описание...
        film.setDescription("— Лишь Машинное Правоверие несёт совершенство. Лишь оно несёт благодать.\n\n" +
                "Произнеся это своим синтезированным голосом, который эхом разнёсся по внутреннему двору " +
                "Чистой базилики, Элеш Норн, Фирексийский регент и Матерь Машин, ощутила свет этой истины глубоко " +
                "внутри своего священного механического тела. Машинное Правоверие было единственным путём к " +
                "окончательному объединению, путём столь же чистым, непогрешимым и несомненным, как и само её " +
                "блестящее масло.\n\n" +
                "Норн никогда ещё не была так в этом уверена, как сейчас, когда она, сверкая доспехами в " +
                "молочно-белом свете, обвела взглядом собравшихся фирексийцев со своего амвона. То были " +
                "символы достигнутого ею могущества: Чистая базилика, её башни, её металлические шпили, " +
                "подобные фарфору, величественные соборы, устремлённые ввысь, извитые, тонкие и воздушные. " +
                "На мостиках и зубцах реяли багровые знамёна, резко выделяясь на фоне сверкающих построек " +
                "и каменных плит, выстилающих двор.\n\n");
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.put(film);
        });
    }
}
