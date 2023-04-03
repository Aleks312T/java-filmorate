package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.controller.FilmController;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(film, filmController.put(film));
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
                LocalDate.of(199, 10, 10),
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
