package ru.yandex.practicum.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<String, User> users = new HashMap<>();

    @GetMapping
    public ArrayList<User> findAll() {

        return null;
    }

    @PostMapping
    public User create(@RequestBody User user) {

        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {


        return user;
    }
}