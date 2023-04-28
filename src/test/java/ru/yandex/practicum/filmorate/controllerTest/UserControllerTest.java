package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserControllerTest {
    static UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    void shouldPostUserOK() {
        User user = new User("login",
                "qwerty@mail.ru",
                LocalDate.of(1999, 10, 10));
        assertEquals(user, userController.create(user));
    }

    @Test
    void shouldUseLoginForEmptyNameOK() {
        User user = new User("login",
                "qwerty@mail.ru",
                LocalDate.of(1999, 10, 10));
        User result = userController.create(user);
        assertEquals(user, result);
        assertEquals("login", result.getName());
    }

    @Test
    void shouldPutSameUserAgainOK() {
        User user1 = new User("login",
                "qwerty@mail.ru",
                LocalDate.of(1999, 10, 10));
        user1.setId(1);

        User user2 = new User("login",
                "qwerty@mail.ru",
                LocalDate.of(1999, 10, 10));
        user2.setId(1);

        assertEquals(user1, userController.create(user1));
        assertEquals(user1, userController.put(user2));
    }

    @Test
    void shouldChangeUserOK() {
        User user1 = new User("login1",
                "qwerty@mail.ru",
                LocalDate.of(1999, 10, 10));
        user1.setId(1);

        User user2 = new User("login2",
                "qwerty@mail.ru",
                LocalDate.of(1999, 10, 10));
        user2.setId(1);

        assertEquals(user1, userController.create(user1));
        assertEquals(user2, userController.put(user2));
    }

    @Test
    void shouldGetUsersOK() {
        User user1 = new User("login1",
                "qwerty1@mail.ru",
                LocalDate.of(1999, 10, 10));
        user1.setId(1);

        User user2 = new User("login2",
                "qwerty2@mail.ru",
                LocalDate.of(1999, 10, 10));
        user1.setId(2);

        assertEquals(user1, userController.create(user1));
        assertEquals(user2, userController.create(user2));

        Map<Integer, User> users = new HashMap<>();
        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);

        //Надо проверять всех пользователей, т.к. сравнение assertEquals не работает с мапами
        Collection<User> result = userController.findAll();
        for (User currentUser: result) {
            Integer currentId = currentUser.getId();
            assertTrue(users.containsKey(currentId));
            assertEquals(users.get(currentId), currentUser);
        }
    }

    @Test
    void shouldGetUsersAfterChangesOK() {
        User user1 = new User("login1",
                "qwerty1@mail.ru",
                LocalDate.of(1999, 10, 10));
        user1.setId(1);

        User user2 = new User("login2",
                "qwerty2@mail.ru",
                LocalDate.of(1999, 10, 10));
        user2.setId(2);

        User newUser1 = new User("login111",
                "qwerty1@mail.ru",
                LocalDate.of(1999, 10, 10));
        newUser1.setId(1);

        assertEquals(user1, userController.create(user1));
        assertEquals(user2, userController.create(user2));
        assertEquals(newUser1, userController.put(newUser1));

        Map<Integer, User> users = new HashMap<>();
        users.put(newUser1.getId(), newUser1);
        users.put(user2.getId(), user2);

        //Надо проверять всех пользователей, т.к. сравнение assertEquals не работает с мапами
        Collection<User> result = userController.findAll();
        for (User currentUser: result) {
            Integer currentId = currentUser.getId();
            assertTrue(users.containsKey(currentId));
            assertEquals(users.get(currentId), currentUser);
        }
    }

    @Test
    void shouldNotPutUnknownUser() {
        User user = new User("login",
                "qwerty@mail.ru",
                LocalDate.of(1999, 10, 10));
        Assertions.assertThrows(RuntimeException.class, () -> {
            userController.put(user);
        });
    }

    @Test
    void shouldThrowNullPointerExceptionBecauseOfNull() throws NullPointerException {
        User user = null;
        Assertions.assertThrows(NullPointerException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void shouldThrowObjectAlreadyExistExceptionBecauseOfId() throws ObjectAlreadyExistException {
        User user1 = new User("login1",
                "qwerty@mail.ru",
                LocalDate.of(1999, 10, 10));
        user1.setId(1);

        User user2 = new User("login2",
                "qwerty@mail.ru",
                LocalDate.of(1999, 10, 10));
        user2.setId(1);

        userController.create(user1);
        Assertions.assertThrows(ObjectAlreadyExistException.class, () -> {
            userController.create(user2);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfBadEmail1() throws ValidationException {
        User user = new User("login",
                "qwertymail.ru",
                LocalDate.of(1999, 10, 10));
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfEmptyEmail() throws ValidationException {
        User user = new User("login",
                "             ",
                LocalDate.of(1999, 10, 10));
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfBirthday() throws ValidationException {
        User user = new User("login",
                "qwerty@mail.ru",
                LocalDate.of(1999, 10, 10));
        user.setBirthday(LocalDate.of(19999, 11, 11));
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfSpacesInLogin() throws ValidationException {
        User user = new User("l o g i n",
                "qwerty@mail.ru",
                LocalDate.of(1999, 10, 10));
        user.setBirthday(LocalDate.of(19999, 11, 11));
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfEmptyLogin() throws ValidationException {
        User user = new User("",
                "qwerty@mail.ru",
                LocalDate.of(1999, 10, 10));
        user.setBirthday(LocalDate.of(19999, 11, 11));
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }
}
