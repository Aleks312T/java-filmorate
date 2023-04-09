package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NamelessObjectException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private Integer id = 0;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос Get /users.");
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен запрос Post /users.");
        if (users.containsKey(user.getId())) {
            String errorMessage = "Пользователь с Id " + user.getId() + " уже существует.";
            log.warn(errorMessage);
            throw new ObjectAlreadyExistException();
        } else
        if (validateUser(user)) {
            log.trace("Пользователь {} прошел валидацию", user.getId());
            if (user.getId() == 0)
                user.setId(++id);
            users.put(user.getId(), user);
        } else {
            String errorMessage = "Пользователь " + user.getId() + " не прошел валидацию";
            log.warn(errorMessage);
            throw new ValidationException();
        }
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        log.info("Получен запрос Put /users.");
        if (!users.containsKey(user.getId()) || user.getId() == 0) {
            String errorMessage = "На обновление пришел пользователь с неизвестным Id = " + user.getId();
            log.warn(errorMessage);

            throw new NamelessObjectException();
        } else
        if (validateUser(user)) {
            log.trace("Пользователь {} прошел валидацию", user.getId());
            //Не выводим ошибку о наличии пользователя из-за метода put
            users.put(user.getId(), user);
        } else {
            String errorMessage = "Пользователь " + user.getId() + " не прошел валидацию";
            log.warn(errorMessage);
            throw new ValidationException();
        }
        return user;
    }

    private boolean validateUser(User user) {
        //Проверка nonNull аргумента
        if (user.getName() == null)
            user.setName(user.getLogin());
        if (user.getName().isBlank())
            user.setName(user.getLogin());

        return (!user.getEmail().isBlank())
                && (user.getEmail().contains("@"))
                && (!user.getLogin().isEmpty())
                && (!user.getLogin().contains(" "))
                && (user.getBirthday().isBefore(LocalDate.now()));
    }
}