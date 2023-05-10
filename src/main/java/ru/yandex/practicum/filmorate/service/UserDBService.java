package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.user.impl.UserStorageDBImpl;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDBService {
    private static final Logger log = LoggerFactory.getLogger(UserDBService.class);

    private final UserStorageDBImpl userStorageDB;

    public User create(User user) {
        if (user == null) {
            String errorMessage = "Отсутствуют входные данные.";
            log.warn(errorMessage);
            throw new NullPointerException(errorMessage);
        } else
        if (user.getId() != null && userStorageDB.getUser(user.getId()) != null) {
            String errorMessage = "Такой пользователь уже есть.";
            log.warn(errorMessage);
            throw new ObjectAlreadyExistException(errorMessage);
        }
        if (validateUser(user))
            return userStorageDB.createUser(user);
        else {
            String errorMessage = "Пользователь не прошел валидацию.";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }

    public Collection<User> findAll() {
        return userStorageDB.getAllUsers();
    }

    public User put(User user) {
        if (user == null) {
            String errorMessage = "Отсутствуют входные данные.";
            log.warn(errorMessage);
            throw new NullPointerException(errorMessage);
        } else
        if (user.getId() == null) {
            String errorMessage = "Отсутствует входной идентификатор.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
        if (!validateUser(user)) {
            String errorMessage = "Пользователь не прошел валидацию.";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        } else
        if (userStorageDB.getUser(user.getId()) == null) {
            String errorMessage = "Пользователя с Id " + user.getId() + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
            return userStorageDB.updateUser(user);
    }

    public User getUser(Integer userId) {
        if (userId == null) {
            String errorMessage = "Отсутствует входной идентификатор";
            log.warn(errorMessage);
            throw new NullPointerException(errorMessage);
        }
        User result = userStorageDB.getUser(userId);
        if (result == null) {
            String errorMessage = "Пользователя с Id " + userId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
            return result;
    }

    public User addFriend(Integer userId, Integer friendId) {
        if (userId == null || friendId == null) {
            String errorMessage = "Отсутствует входной идентификатор";
            log.warn(errorMessage);
            throw new NullPointerException(errorMessage);
        } else
        if (userId.equals(friendId)) {
            String errorMessage = "Нельзя добавить в друзья самого себя";
            log.warn(errorMessage);
            throw new InputMismatchException(errorMessage);
        } else
        if (userStorageDB.getUser(userId) == null) {
            String errorMessage = "Пользователь " + userId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
        if (userStorageDB.getUser(friendId) == null) {
            String errorMessage = "Пользователь " + friendId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else {
            return userStorageDB.addFriend(userId, friendId);
        }
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        if (Objects.equals(userId, friendId)) {
            String errorMessage = "Нельзя добавить в друзья самого себя";
            log.warn(errorMessage);
            throw new InputMismatchException(errorMessage);
        } else
        if (userStorageDB.getUser(userId) == null) {
            String errorMessage = "Пользователь " + userId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
        if (userStorageDB.getUser(friendId) == null) {
            String errorMessage = "Пользователь " + friendId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else {
            return userStorageDB.deleteFriend(userId, friendId);
        }
    }

    public Collection<User> getFriends(Integer userId) {
        if (userId == null) {
            String errorMessage = "Отсутствует входной идентификатор";
            log.warn(errorMessage);
            throw new NullPointerException(errorMessage);
        } else
        if (userStorageDB.getUser(userId) == null) {
            String errorMessage = "Пользователь " + userId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else {
            return userStorageDB.getFriends(userId);
        }
    }

    public Collection<User> getCommonFriends(Integer firstUserId, Integer secondUserId) {
        if (userStorageDB.getUser(firstUserId) == null) {
            String errorMessage = "Пользователь " + firstUserId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
        if (userStorageDB.getUser(secondUserId) == null) {
            String errorMessage = "Пользователь " + secondUserId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else {
            return userStorageDB.getCommonFriends(firstUserId, secondUserId);
        }
    }

    private boolean validateUser(User user) {
        log.trace("Вызов функции {} с входными данными {}.",
                "validateUser", user);
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
