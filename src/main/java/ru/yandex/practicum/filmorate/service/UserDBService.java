package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NamelessObjectException;
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
            validationError(errorMessage, "NullPointerException");
        } else
        if (user.getId() != null && userStorageDB.getUser(user.getId()) != null) {
            String errorMessage = "Такой пользователь уже есть.";
            validationError(errorMessage, "ObjectAlreadyExistException");
        }
        if (validateUser(user))
            return userStorageDB.createUser(user);
        else {
            String errorMessage = "Пользователь не прошел валидацию.";
            validationError(errorMessage, "ValidationException");
        }
        return user;
    }

    public Collection<User> findAll() {
        return userStorageDB.getAllUsers();
    }

    public User put(User user) {
        if (user == null) {
            String errorMessage = "Отсутствуют входные данные.";
            validationError(errorMessage, "NullPointerException");
        } else
        if (user.getId() == null) {
            String errorMessage = "Отсутствует входной идентификатор.";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else
        if (!validateUser(user)) {
            String errorMessage = "Пользователь не прошел валидацию.";
            validationError(errorMessage, "ValidationException");
        } else
        if (userStorageDB.getUser(user.getId()) == null) {
            String errorMessage = "Пользователя с Id " + user.getId() + " нет.";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else
            return userStorageDB.updateUser(user);
        return user;
    }

    public User getUser(Integer userId) {
        if (userId == null) {
            String errorMessage = "Отсутствует входной идентификатор";
            validationError(errorMessage, "NullPointerException");
        }
        User result = userStorageDB.getUser(userId);
        if (result == null) {
            String errorMessage = "Пользователя с Id " + userId + " нет.";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else
            return result;
        return result;
    }

    public User addFriend(Integer userId, Integer friendId) {
        if (userId == null || friendId == null) {
            String errorMessage = "Отсутствует входной идентификатор";
            validationError(errorMessage, "NullPointerException");
        } else
        if (userId.equals(friendId)) {
            String errorMessage = "Нельзя добавить в друзья самого себя";
            validationError(errorMessage, "InputMismatchException");
        } else
        if (userStorageDB.getUser(userId) == null) {
            String errorMessage = "Пользователь " + userId + " не найден";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else
        if (userStorageDB.getUser(friendId) == null) {
            String errorMessage = "Пользователь " + friendId + " не найден";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else {
            return userStorageDB.addFriend(userId, friendId);
        }
        return getUser(userId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        if (Objects.equals(userId, friendId)) {
            String errorMessage = "Нельзя добавить в друзья самого себя";
            validationError(errorMessage, "InputMismatchException");
        } else
        if (userStorageDB.getUser(userId) == null) {
            String errorMessage = "Пользователь " + userId + " не найден";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else
        if (userStorageDB.getUser(friendId) == null) {
            String errorMessage = "Пользователь " + friendId + " не найден";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else {
            return userStorageDB.deleteFriend(userId, friendId);
        }
        return getUser(userId);
    }

    public Collection<User> getFriends(Integer userId) {
        if (userId == null) {
            String errorMessage = "Отсутствует входной идентификатор";
            validationError(errorMessage, "NullPointerException");
        } else
        if (userStorageDB.getUser(userId) == null) {
            String errorMessage = "Пользователь " + userId + " не найден";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else {
            return userStorageDB.getFriends(userId);
        }
        return null;
    }

    public Collection<User> getCommonFriends(Integer firstUserId, Integer secondUserId) {
        if (userStorageDB.getUser(firstUserId) == null) {
            String errorMessage = "Пользователь " + firstUserId + " не найден";
            validationError(errorMessage, "ObjectDoesntExistException");
        } else
        if (userStorageDB.getUser(secondUserId) == null) {
            String errorMessage = "Пользователь " + secondUserId + " не найден";
            validationError(errorMessage, "ObjectDoesntExistException");
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else {
            return userStorageDB.getCommonFriends(firstUserId, secondUserId);
        }
        return null;
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

    private void validationError(String errorMessage, String exception) {
        log.warn(errorMessage);
        switch (exception) {
            case "ObjectDoesntExistException":
                throw new ObjectDoesntExistException(errorMessage);
            case "ObjectAlreadyExistException":
                throw new ObjectAlreadyExistException(errorMessage);
            case "NullPointerException":
                throw new NullPointerException(errorMessage);
            case "InputMismatchException":
                throw new InputMismatchException(errorMessage);
            case "ValidationException":
                throw new ValidationException(errorMessage);
            case "NamelessObjectException":
                throw new NamelessObjectException(errorMessage);
            default:
                throw new RuntimeException(errorMessage);
        }
    }
}
