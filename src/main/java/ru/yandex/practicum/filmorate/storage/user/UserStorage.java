package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAll();
    User create(User user);
    User put(User user);
    public boolean containUser(User user);
    public boolean containUserId(int id);
    public User getUserById(int id);
}
