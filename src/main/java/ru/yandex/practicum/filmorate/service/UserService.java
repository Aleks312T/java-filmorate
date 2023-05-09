package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User put(User user) {
        return userStorage.put(user);
    }

    public User getUser(Integer userId) {
        if (userId == null) {
            String errorMessage = "Отсутствует входной идентификатор";
            log.warn(errorMessage);
            throw new NullPointerException(errorMessage);
        }
        User result = userStorage.getUserById(userId);
        if (result == null) {
            String errorMessage = "Пользователя с Id " + userId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
            return result;
    }

    //Возможно решение стоит добавить в InMemoryUserStorage
    public User addFriend(Integer userId, Integer friendId) {
        if (userId == friendId) {
            String errorMessage = "Нельзя добавить в друзья самого себя";
            log.warn(errorMessage);
            throw new InputMismatchException(errorMessage);
        } else
        if (userStorage.getUserById(userId) == null) {
            String errorMessage = "Пользователь " + userId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
        if (userStorage.getUserById(friendId) == null) {
            String errorMessage = "Пользователь " + friendId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else {
            //Добавить друга пользователю
            User user = userStorage.findAll().stream()
                    .filter(currentUser -> currentUser.getId().equals(userId))
                    .findFirst().get();
            user.getFriends().add(friendId);
            //Добавить друга другу
            User friend = userStorage.findAll().stream()
                    .filter(currentFriend -> currentFriend.getId().equals(friendId))
                    .findFirst().get();
            friend.getFriends().add(userId);
            return user;
        }
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        if (userId == friendId) {
            String errorMessage = "Нельзя добавить в друзья самого себя";
            log.warn(errorMessage);
            throw new InputMismatchException(errorMessage);
        } else
        if (userStorage.getUserById(userId) == null) {
            String errorMessage = "Пользователь " + userId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
        if (userStorage.getUserById(friendId) == null) {
            String errorMessage = "Пользователь " + friendId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else {
            //Удалить друга у пользователя
            User user = userStorage.findAll().stream()
                    .filter(currentUser -> currentUser.getId().equals(userId))
                    .findFirst().get();
            if (user.getFriends().contains(friendId))
                user.getFriends().remove(friendId);
            else {
                String errorMessage = "Пользователь " + userId + " не является другом " + friendId;
                log.warn(errorMessage);
                throw new InputMismatchException(errorMessage);
            }

            //Добавить друга у друга
            User friend = userStorage.findAll().stream()
                    .filter(currentFriend -> currentFriend.getId().equals(friendId))
                    .findFirst().get();
            if (friend.getFriends().contains(userId))
                friend.getFriends().remove(userId);
            else {
                String errorMessage = "Пользователь " + friendId + " не является другом " + userId;
                log.warn(errorMessage);
                throw new InputMismatchException(errorMessage);
            }

            return user;
        }
    }

    public Collection<User> getCommonFriends(Integer firstUserId, Integer secondUserId) {
        if (userStorage.getUserById(firstUserId) == null) {
            String errorMessage = "Пользователь " + firstUserId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
        if (userStorage.getUserById(secondUserId) == null) {
            String errorMessage = "Пользователь " + secondUserId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else {
            Set<User> result;

            //Надо использовать именно локальный getFriends(firstUserId) а не user1.getFriends()
            result = getFriends(firstUserId).stream()
                    .filter(getFriends(secondUserId)::contains)
                    .collect(Collectors.toSet());

            return result;
        }
    }

    public Collection<User> getFriends(Integer userId) {
        if (userStorage.getUserById(userId) == null) {
            String errorMessage = "Пользователь " + userId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else {
            Set<User> result = new HashSet<>();
            Set<Integer> friends;

            User user = userStorage.getUserById(userId);
            friends = user.getFriends();

            for (Integer friendId : friends)
                result.add(userStorage.getUserById(friendId));

            return result;
        }
    }
}
