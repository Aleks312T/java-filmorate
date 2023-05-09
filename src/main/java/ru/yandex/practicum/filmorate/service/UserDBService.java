package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.interf.UserStorageDB;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDBService {
    private static final Logger log = LoggerFactory.getLogger(UserDBService.class);
    private final UserStorageDB userStorageDB;

    @Autowired
    public UserDBService(UserStorageDB userStorage) {
        this.userStorageDB = userStorage;
    }

    public User create(User user) {
        return userStorageDB.createUser(user);
    }

    public Collection<User> findAll() {
        return userStorageDB.getAllUsers();
    }

    public User put(User user) {
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
        if (userId == friendId) {
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

    //TODO: нужно будет доделать
    public User deleteFriend(Integer userId, Integer friendId) {
        return null;
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

    //TODO: нужно будет доделать
    public Collection<User> getCommonFriends(Integer firstUserId, Integer secondUserId) {
        return null;
    }
}
