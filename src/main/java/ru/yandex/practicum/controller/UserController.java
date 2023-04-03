package ru.yandex.practicum.controller;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

@RestController
@RequestMapping("/Users")
public class UserController {
    private final Map<String, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public ArrayList<User> findAll() {
        log.info("Получен запрос Get /Users.");
        log.debug("Текущее количество пользователей: {}", users.size());

        return null;
    }

    @PostMapping
    public User create(@RequestBody @NonNull User user) {
        log.info("Получен запрос Post /Users.");
        if(validateUser(user))
        {
            log.trace("Пользователь прошел валидацию");
            //Думал перенести это в validateUser, но по логическим соображениям не стал
            if(user.getName().isBlank())
                user.setName(user.getLogin());
        } else
        {
            throw new ValidationException();
        }

        return user;
    }

    @PutMapping
    public User put(@RequestBody @NonNull User user) {
        log.info("Получен запрос Put /Users.");
        if(validateUser(user))
        {
            log.trace("Пользователь прошел валидацию");
            if(user.getName().isBlank())
                user.setName(user.getLogin());
        } else
        {
            throw new ValidationException();
        }

        return user;
    }

    private boolean validateUser(User user)
    {
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