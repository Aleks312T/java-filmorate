package ru.yandex.practicum.filmorate.dao.interf;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorageDB {
    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUser(int id);

    User addFriend(Integer userId, Integer friendId);

    public List<User> getFriends(Integer userId);

    User deleteFriend(Integer userId, Integer friendId);

    Collection<User> getCommonFriends(Integer firstUserId, Integer secondUserId);
}
