package ru.yandex.practicum.controller;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/Users")
public class UserController {
    private final Map<String, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос Get /Users.");
        log.debug("Текущее количество пользователей: {}", users.size());

        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен запрос Post /Users.");
        if(validateUser(user))
        {
            log.trace("Пользователь прошел валидацию");
            if(users.containsKey(user.getEmail()))
                throw new ObjectAlreadyExistException("Пользователь с почтой " + user.getEmail() + " уже существует");
            users.put(user.getEmail(), user);
        } else
        {
            String errorMessage = "Пользователь не прошел валидацию";
            log.warn(errorMessage);
            throw new ValidationException();
        }

        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        log.info("Получен запрос Put /Users.");
        if(validateUser(user))
        {
            log.trace("Пользователь прошел валидацию");
            //Не выводим ошибку о наличии пользователя из-за метода put
            users.put(user.getEmail(), user);
        } else
        {
            String errorMessage = "Пользователь не прошел валидацию";
            log.warn(errorMessage);
            throw new ValidationException();
        }

        return user;
    }

    private boolean validateUser(User user)
    {
        //Проверка nonNull аргумента
        if(user.getName().isBlank())
            user.setName(user.getLogin());

        //isBlank, чтобы отсеять пробелы
        if(user.getEmail().isBlank() || !user.getEmail().contains("@"))
            return false;
        if(user.getLogin().isEmpty() || user.getLogin().contains(" "))
            return false;
        if(user.getBirthday() != null)
            return !user.getBirthday().isAfter(LocalDate.now());

        return true;
    }
}