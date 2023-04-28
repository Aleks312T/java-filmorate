package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос Get /users.");
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос Post /users.");
        return userService.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        log.info("Получен запрос Put /users.");
        return userService.put(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@Valid @RequestBody
                          @PathVariable("id") int userId,
                          @PathVariable("friendId") int friendId) {
        log.info("Получен запрос Put /{}/friends/{}.", userId, friendId);
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@Valid @RequestBody
                             @PathVariable("id") int userId,
                             @PathVariable("friendId") int friendId) {
        log.info("Получен запрос Delete /{}/friends/{}.", userId, friendId);
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@Valid @RequestBody @PathVariable("id") int userId) {
        log.info("Получен запрос Get /{}/friends.", userId);
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}")
    public User getUser(@Valid @RequestBody @PathVariable("id") int userId) {
        log.info("Получен запрос Get /{}.", userId);
        return userService.getUser(userId);
    }
}