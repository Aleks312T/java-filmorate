package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dao.impl.UserStorageDBImpl;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserDBService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserControllerTest {
    static UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController(new UserDBService(new UserStorageDBImpl(new JdbcTemplate())));
    }

    @Test
    void shouldPostUserOK() {
        User user = User.builder()
                .login("login")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        assertEquals(user, userController.create(user));
    }

    @Test
    void shouldUseLoginForEmptyNameOK() {
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
    void shouldPutSameUserAgainOK() {
        User user1 = User.builder()
                .login("login")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        User user2 = User.builder()
                .login("login")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();

        assertEquals(user1, userController.create(user1));
        assertEquals(user1, userController.put(user2));
    }

    @Test
    void shouldChangeUserOK() {
        User user1 = User.builder()
                .login("login1")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        User user2 = User.builder()
                .login("login2")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2002, 2, 2))
                .id(1)
                .build();

        assertEquals(user1, userController.create(user1));
        assertEquals(user2, userController.put(user2));
    }

    @Test
    void shouldAddToFriendsOK() {
        User user1 = User.builder()
                .login("login1")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        User user2 = User.builder()
                .login("login2")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2002, 2, 2))
                .id(2)
                .build();

        assertEquals(user1, userController.create(user1));
        assertEquals(user2, userController.create(user2));

        //Проверка, что пользователи еще не в друзьях
        assertEquals(new HashSet<>(), userController.getUser(1).getFriends());
        assertEquals(new HashSet<>(), userController.getUser(2).getFriends());

        //Добавим друзей
        userController.addFriend(1, 2);
        Set<Integer> result1 = new HashSet<>();
        result1.add(2);
        Set<Integer> result2 = new HashSet<>();
        result2.add(1);

        //Проверяем
        assertEquals(result1, userController.getUser(1).getFriends());
        assertEquals(result2, userController.getUser(2).getFriends());
    }

    @Test
    void shouldAddAndRemoveToFriendsOK() {
        User user1 = User.builder()
                .login("login1")
                .email("qwerty1@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        User user2 = User.builder()
                .login("login2")
                .email("qwerty2@mail.ru")
                .birthday(LocalDate.of(2002, 2, 2))
                .id(2)
                .build();
        User user3 = User.builder()
                .login("login3")
                .email("qwerty3@mail.ru")
                .birthday(LocalDate.of(2003, 3, 3))
                .id(3)
                .build();

        assertEquals(user1, userController.create(user1));
        assertEquals(user2, userController.create(user2));
        assertEquals(user3, userController.create(user3));

        //Проверка, что пользователи еще не в друзьях
        assertEquals(new HashSet<>(), userController.getUser(1).getFriends());
        assertEquals(new HashSet<>(), userController.getUser(2).getFriends());
        assertEquals(new HashSet<>(), userController.getUser(3).getFriends());

        //Добавим друзей
        userController.addFriend(1, 2);
        userController.addFriend(2, 3);
        Set<Integer> result1 = new HashSet<>();
        result1.add(2);
        Set<Integer> result2 = new HashSet<>();
        result2.add(1);
        result2.add(3);
        Set<Integer> result3 = new HashSet<>();
        result3.add(2);

        //Проверяем
        assertEquals(result1, userController.getUser(1).getFriends());
        assertEquals(result2, userController.getUser(2).getFriends());
        assertEquals(result3, userController.getUser(3).getFriends());

        //Удалим из друзей 1 и 2 пользователей
        userController.deleteFriend(1, 2);
        //У первого теперь нет друзей
        result1.clear();
        //Пользователи 2 и 3 все еще в друзьях
        result2.clear();
        result2.add(3);
        //У третьего ничего не изменилось

        //Проверяем
        assertEquals(result1, userController.getUser(1).getFriends());
        assertEquals(result2, userController.getUser(2).getFriends());
        assertEquals(result3, userController.getUser(3).getFriends());
    }

    @Test
    void shouldGetFriendsOK() {
        User user1 = User.builder()
                .login("login1")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        User user2 = User.builder()
                .login("login2")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2002, 2, 2))
                .id(2)
                .build();

        assertEquals(user1, userController.create(user1));
        assertEquals(user2, userController.create(user2));

        //Добавим друзей
        userController.addFriend(1, 2);
        Set<User> result1 = new HashSet<>();
        result1.add(user2);
        Set<User> result2 = new HashSet<>();
        result2.add(user1);

        //Проверяем
        assertEquals(result1, userController.getFriends(1));
        assertEquals(result2, userController.getFriends(2));
    }

    @Test
    void shouldGetCommonFriendsOK() {
        User user1 = User.builder()
                .login("login1")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        User user2 = User.builder()
                .login("login2")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2002, 2, 2))
                .id(2)
                .build();
        User user3 = User.builder()
                .login("login3")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2003, 3, 4))
                .id(3)
                .build();
        User user4 = User.builder()
                .login("login4")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2004, 4, 4))
                .id(4)
                .build();

        assertEquals(user1, userController.create(user1));
        assertEquals(user2, userController.create(user2));
        assertEquals(user3, userController.create(user3));
        assertEquals(user4, userController.create(user4));

        //Добавим друзей
        userController.addFriend(1, 2);
        userController.addFriend(1, 3);
        userController.addFriend(2, 3);
        userController.addFriend(2, 4);
        userController.addFriend(3, 4);

        Set<User> result1 = new HashSet<>();
        result1.add(user2);
        result1.add(user3);
        Set<User> result2 = new HashSet<>();
        result2.add(user3);
        Set<User> result3 = new HashSet<>();
        result3.add(user2);
        Set<User> result4 = new HashSet<>();
        result4.add(user1);
        result4.add(user4);

        //Проверяем
        assertEquals(result1, userController.getCommonFriends(1, 4));
        assertEquals(result2, userController.getCommonFriends(1, 2));
        assertEquals(result3, userController.getCommonFriends(1, 3));
        assertEquals(result4, userController.getCommonFriends(2, 3));
    }

    @Test
    void shouldGetCommonFriendsAfterDeleteOK() {
        User user1 = User.builder()
                .login("login1")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        User user2 = User.builder()
                .login("login2")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2002, 2, 2))
                .id(2)
                .build();
        User user3 = User.builder()
                .login("login3")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2003, 3, 4))
                .id(3)
                .build();
        User user4 = User.builder()
                .login("login4")
                .email("qwerty@mail.ru")
                .birthday(LocalDate.of(2004, 4, 4))
                .id(4)
                .build();

        assertEquals(user1, userController.create(user1));
        assertEquals(user2, userController.create(user2));
        assertEquals(user3, userController.create(user3));
        assertEquals(user4, userController.create(user4));

        //Добавим друзей
        userController.addFriend(1, 2);
        userController.addFriend(1, 3);
        userController.addFriend(2, 4);
        userController.addFriend(3, 4);

        Set<User> result1 = new HashSet<>();
        result1.add(user2);
        result1.add(user3);
        Set<User> result2 = new HashSet<>();
        result2.add(user3);

        //Проверяем
        assertEquals(result1, userController.getCommonFriends(1, 4));
        userController.deleteFriend(1, 2);
        assertEquals(result2, userController.getCommonFriends(1, 4));
    }

    @Test
    void shouldGetUsersOK() {
        User user1 = User.builder()
                .login("login1")
                .email("qwerty1@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        User user2 = User.builder()
                .login("login2")
                .email("qwerty2@mail.ru")
                .birthday(LocalDate.of(2002, 2, 2))
                .id(2)
                .build();

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
        User user1 = User.builder()
                .login("login1")
                .email("qwerty1@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        User user2 = User.builder()
                .login("login2")
                .email("qwerty2@mail.ru")
                .birthday(LocalDate.of(2002, 2, 2))
                .id(2)
                .build();

        User newUser1 = User.builder()
                .login("login111")
                .email("qwerty1@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();

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
    void shouldNotPutUnknownUser() throws RuntimeException {
        User user = User.builder()
                .login("login1")
                .email("qwerty1@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
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
        User user1 = User.builder()
                .login("login1")
                .email("qwerty1@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        User user2 = User.builder()
                .login("login2")
                .email("qwerty2@mail.ru")
                .birthday(LocalDate.of(2002, 2, 2))
                .id(1)                      //Неверный ID
                .build();

        userController.create(user1);
        Assertions.assertThrows(ObjectAlreadyExistException.class, () -> {
            userController.create(user2);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfBadEmail1() throws ValidationException {
        User user = User.builder()
                .login("login1")
                .email("qwertymail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfEmptyEmail() throws ValidationException {
        User user = User.builder()
                .login("login1")
                .email("               ")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfBirthday() throws ValidationException {
        User user = User.builder()
                .login("login1")
                .email("qwerty1@mail.ru")
                .birthday(LocalDate.of(200001, 1, 1))
                .id(1)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfSpacesInLogin() throws ValidationException {
        User user = User.builder()
                .login("l o g i n 1")
                .email("qwerty1@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void shouldThrowValidationExceptionBecauseOfEmptyLogin() throws ValidationException {
        User user = User.builder()
                .login("")
                .email("qwerty1@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }
}
