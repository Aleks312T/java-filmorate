package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.controller.UserController;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.time.Duration;
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
    void beforeEach()
    {
        userController = new UserController();
    }

    @Test
    void shouldPostUserOK() {
        User user = new User(1,
                "qwerty@mail.ru",
                "login",
                "name");
        assertEquals(user, userController.create(user));
    }

    @Test
    void shouldPutUserOK() {
        User user = new User(1,
                "qwerty@mail.ru",
                "login",
                "name");
        assertEquals(user, userController.put(user));
    }

    @Test
    void shouldPutSameUserAgainOK() {
        User user1 = new User(1,
                "qwerty@mail.ru",
                "login",
                "name");
        User user2 = new User(1,
                "qwerty@mail.ru",
                "login",
                "name");
        assertEquals(user1, userController.create(user1));
        assertEquals(user1, userController.put(user2));
    }

    @Test
    void shouldChangeUserOK() {
        User user1 = new User(1,
                "qwerty@mail.ru",
                "login1",
                "name1");
        User user2 = new User(1,
                "qwerty@mail.ru",
                "login2",
                "name2");
        assertEquals(user1, userController.create(user1));
        assertEquals(user2, userController.put(user2));
    }

    @Test
    void shouldGetUsersOK() {
        User user1 = new User(1,
                "qwerty1@mail.ru",
                "login1",
                "name1");
        User user2 = new User(1,
                "qwerty2@mail.ru",
                "login2",
                "name2");
        assertEquals(user1, userController.create(user1));
        assertEquals(user2, userController.create(user2));

        Map<String, User> users = new HashMap<>();
        users.put(user1.getName(), user1);
        users.put(user2.getName(), user2);

        //Надо проверять всех пользователей, т.к. сравнение assertEquals не работает с мапами
        Collection<User> result = userController.findAll();
        for(User currentUser: result)
        {
            String currentName = currentUser.getName();
            assertTrue(users.containsKey(currentName));
            assertEquals(users.get(currentName), currentUser);
        }
    }

    @Test
    void shouldGetUsersAfterChangesOK() {
        User user1 = new User(1,
                "qwerty1@mail.ru",
                "login1",
                "name1");
        User user2 = new User(1,
                "qwerty2@mail.ru",
                "login2",
                "name2");
        User newUser1 = new User(1,
                "qwerty1@mail.ru",
                "login111",
                "name111");
        assertEquals(user1, userController.create(user1));
        assertEquals(user2, userController.create(user2));
        assertEquals(newUser1, userController.put(newUser1));

        Map<String, User> users = new HashMap<>();
        users.put(newUser1.getName(), newUser1);
        users.put(user2.getName(), user2);

        //Надо проверять всех пользователей, т.к. сравнение assertEquals не работает с мапами
        Collection<User> result = userController.findAll();
        for(User currentUser: result)
        {
            String currentName = currentUser.getName();
            assertTrue(users.containsKey(currentName));
            assertEquals(users.get(currentName), currentUser);
        }
    }
}
