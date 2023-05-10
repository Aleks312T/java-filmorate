package ru.yandex.practicum.filmorate.controllerTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    private final UserController userController;
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

    @BeforeEach
    void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
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
    void shouldThrowExceptionBecauseOfEmptyEmail() {
        User user = User.builder()
                .login("login1")
                .email("               ")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        Assertions.assertThrows(Exception.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void shouldThrowExceptionBecauseOfBadEmail1() {
        User user = User.builder()
                .login("login1")
                .email("qwertymail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        Assertions.assertThrows(Exception.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void shouldThrowExceptionBecauseOfBirthday() {
        User user = User.builder()
                .login("login1")
                .email("qwerty1@mail.ru")
                .birthday(LocalDate.of(200001, 1, 1))
                .id(1)
                .build();
        Assertions.assertThrows(Exception.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void shouldThrowExceptionBecauseOfSpacesInLogin() {
        User user = User.builder()
                .login("l o g i n 1")
                .email("qwerty1@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        Assertions.assertThrows(Exception.class, () -> {
            userController.create(user);
        });
    }

    @Test
    void shouldThrowExceptionBecauseOfEmptyLogin() {
        User user = User.builder()
                .login("")
                .email("qwerty1@mail.ru")
                .birthday(LocalDate.of(2001, 1, 1))
                .id(1)
                .build();
        Assertions.assertThrows(Exception.class, () -> {
            userController.create(user);
        });
    }
}
