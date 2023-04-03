package ru.yandex.practicum.controller;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<String, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public ArrayList<User> findAll() {

        return null;
    }

    @PostMapping
    public User create(@RequestBody @NonNull User user) {
        if(validateUser(user))
        {
            //Думал перенести это в validateUser, но по логическим соображениям не стал
            if(user.getName().isBlank())
                user.setName(user.getLogin());
        } else
        {
            throw new InputMismatchException();
        }

        return user;
    }

    @PutMapping
    public User put(@RequestBody @NonNull User user) {
        if(validateUser(user))
        {
            if(user.getName().isBlank())
                user.setName(user.getLogin());
        } else
        {
            throw new InputMismatchException();
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