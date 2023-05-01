package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NamelessObjectException;
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


    public User getUser(int userId) {
        User result = userStorage.getUserById(userId);
        if(result == null) {
            String errorMessage = "Пользователя с Id " + userId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException();
        } else
            return result;
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
            throw new ObjectDoesntExistException();
        } else
        if(!userStorage.containUserId(friendId)) {
            String errorMessage = "Пользователь " + friendId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException();
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
            throw new ObjectDoesntExistException();
        } else
        if(!userStorage.containUserId(friendId)) {
            String errorMessage = "Пользователь " + friendId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException();
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
                throw new InputMismatchException();
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
                throw new InputMismatchException();
            }

            return user;
        }
    }

    public Collection<User> getCommonFriends(int firstUserId, int secondUserId) {
        if(!userStorage.containUserId(firstUserId)) {
            String errorMessage = "Пользователь " + firstUserId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException();
        } else
        if(!userStorage.containUserId(secondUserId)) {
            String errorMessage = "Пользователь " + secondUserId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException();
        } else {
            Set<User> result = new HashSet<>();

            User user1 = userStorage.getUserById(firstUserId);
            User user2 = userStorage.getUserById(secondUserId);

            //Надо использовать именно локальный getFriends(firstUserId) а не user1.getFriends()
            result = getFriends(firstUserId).stream()
                    .filter(getFriends(secondUserId)::contains)
                    .collect(Collectors.toSet());

            return result;
        }
    }

    public Collection<User> getFriends(int userId) {
        if(!userStorage.containUserId(userId)) {
            String errorMessage = "Пользователь " + userId + " не найден";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException();
        } else {
            Set<User> result = new HashSet<>();
            Set<Integer> friends;

            User user = userStorage.getUserById(userId);
            friends = user.getFriends();

            for(Integer friendId : friends)
                result.add(userStorage.getUserById(friendId));

            return result;
        }
    }
}
