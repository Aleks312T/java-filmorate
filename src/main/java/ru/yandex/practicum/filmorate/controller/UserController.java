package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
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
    private final Map<String, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос Get /users.");
        log.debug("Текущее количество пользователей: {}", users.size());

        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен запрос Post /users.");
        if(validateUser(user))
        {
            log.trace("Пользователь прошел валидацию");
            if(users.containsKey(user.getEmail()))
                throw new ObjectAlreadyExistException("Пользователь с почтой " + user.getEmail() + " уже существует");
            if(user.getId() == 0)
                user.setId(users.size() + 1);
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
        log.info("Получен запрос Put /users.");
        if(!users.containsKey(user.getEmail()))
        {
            String errorMessage = "Пришел неизвестный фильм на обновление";
            log.warn(errorMessage);
            //Не уверен в том, какое исключение нужно выдавать
            throw new RuntimeException();
        }
        if(validateUser(user))
        {
            log.trace("Пользователь прошел валидацию");
            if(user.getId() == 0)
                user.setId(users.size());
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
        if(user.getName() == null)
            user.setName(user.getLogin());
        if(user.getName().isBlank())
            user.setName(user.getLogin());

        //isBlank, чтобы отсеять пробелы
        if(user.getEmail().isBlank() || !user.getEmail().contains("@"))
            return false;
        if(user.getLogin().isEmpty() || user.getLogin().contains(" "))
            return false;
        return !user.getBirthday().isAfter(LocalDate.now());

    }
}