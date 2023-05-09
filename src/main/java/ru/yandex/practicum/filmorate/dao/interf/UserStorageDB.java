package ru.yandex.practicum.filmorate.dao.interf;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorageDB {
    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(int id);
}
