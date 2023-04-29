package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NamelessObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Set;

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

    public User getUser(int userId) {
        return userStorage.getUserById(userId);
    }

    //Возможно решение стоит добавить в InMemoryUserStorage
    public User addFriend(int userId, int friendId) {
        if(userId == friendId) {
            String errorMessage = "Нельзя добавить в друзья самого себя";
            log.warn(errorMessage);
            throw new InputMismatchException();
        } else
        if(!userStorage.containUserId(userId)) {
            String errorMessage = "Пользователь " + userId + " не найден";
            log.warn(errorMessage);
            throw new NamelessObjectException();
        } else
        if(!userStorage.containUserId(friendId)) {
            String errorMessage = "Пользователь " + friendId + " не найден";
            log.warn(errorMessage);
            throw new NamelessObjectException();
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

    public User deleteFriend(int userId, int friendId) {
        if(userId == friendId) {
            String errorMessage = "Нельзя добавить в друзья самого себя";
            log.warn(errorMessage);
            throw new InputMismatchException();
        } else
        if(!userStorage.containUserId(userId)) {
            String errorMessage = "Пользователь " + userId + " не найден";
            log.warn(errorMessage);
            throw new NamelessObjectException();
        } else
        if(!userStorage.containUserId(friendId)) {
            String errorMessage = "Пользователь " + friendId + " не найден";
            log.warn(errorMessage);
            throw new NamelessObjectException();
        } else {
            //Удалить друга у пользователя
            User user = userStorage.findAll().stream()
                    .filter(currentUser -> currentUser.getId().equals(userId))
                    .findFirst().get();
            if(user.getFriends().contains(friendId))
                user.getFriends().remove(friendId);
            else {
                String errorMessage = "Пользователь " + userId + " не является другом " + friendId;
                log.warn(errorMessage);
                throw new NamelessObjectException();
            }

            //Добавить друга у друга
            User friend = userStorage.findAll().stream()
                    .filter(currentFriend -> currentFriend.getId().equals(friendId))
                    .findFirst().get();
            if(friend.getFriends().contains(userId))
                friend.getFriends().remove(userId);
            else {
                String errorMessage = "Пользователь " + friendId + " не является другом " + userId;
                log.warn(errorMessage);
                throw new NamelessObjectException();
            }

            return user;
        }
    }

    public Collection<User> getFriends(int userId) {
        if(!userStorage.containUserId(userId)) {
            String errorMessage = "Пользователь " + userId + " не найден";
            log.warn(errorMessage);
            throw new NamelessObjectException();
        } else {
            Set<User> result = new HashSet<>();
            Set<Integer> friends = new HashSet<>();

            User user = userStorage.getUserById(userId);
            friends = user.getFriends();

            for(Integer friendId : friends)
                result.add(userStorage.getUserById(friendId));

            return result;
        }
    }
}
