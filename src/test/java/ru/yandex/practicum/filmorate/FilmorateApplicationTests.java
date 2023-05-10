package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmDBService;
import ru.yandex.practicum.filmorate.service.UserDBService;
import ru.yandex.practicum.filmorate.storage.dao.film.impl.FilmStorageDBImpl;
import ru.yandex.practicum.filmorate.storage.dao.user.impl.UserStorageDBImpl;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
	private UserController userController = new UserController(new UserDBService(
			new UserStorageDBImpl(new JdbcTemplate())));
	private FilmController filmController = new FilmController(new FilmDBService(
			new FilmStorageDBImpl(new JdbcTemplate()), new UserStorageDBImpl(new JdbcTemplate())));

	@Test
	public void testFindUserById() {
		User user = User.builder()
				.login("login")
				.email("qwerty@mail.ru")
				.birthday(LocalDate.of(2001, 1, 1))
				.id(1)
				.build();
		User result = userController.create(user);
		assertEquals(user, result);
		assertEquals("login", result.getName());
	}

	@Test
	public void testFindFilmById() {
		Film film = Film.builder()
				.name("qwerty")
				.description("Description")
				.releaseDate(LocalDate.of(1999, 10, 10))
				.duration(120)
				.id(1)
				.mpa(new Mpa(1, "G"))
				.build();
		assertEquals(film, filmController.create(film));
	}
}