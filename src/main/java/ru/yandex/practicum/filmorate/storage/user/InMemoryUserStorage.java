package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NamelessObjectException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 0;

    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    public User create(User user) {
        if (users.containsKey(user.getId())) {
            String errorMessage = "Пользователь с Id " + user.getId() + " уже зарегистрирован.";
            log.warn(errorMessage);
            throw new ObjectAlreadyExistException();
        } else
        if (validateUser(user)) {
            log.trace("Пользователь {} прошел валидацию", user.getId());
            if (user.getId() ==  null || user.getId() == 0)
                user.setId(++id);
            users.put(user.getId(), user);
        } else {
            String errorMessage = "Пользователь " + user.getId() + " не прошел валидацию";
            log.warn(errorMessage);
            throw new ValidationException();
        }
        return user;
    }

    public User put(User user) {
        if (!users.containsKey(user.getId()) || user.getId() == 0) {
            String errorMessage = "На обновление пришел пользователь с неизвестным Id = " + user.getId();
            log.warn(errorMessage);
            throw new NamelessObjectException();
        } else
        if (validateUser(user)) {
            log.trace("Пользователь {} прошел валидацию", user.getId());
            //Не выводим ошибку о наличии пользователя из-за метода put
            users.put(user.getId(), user);
        } else {
            String errorMessage = "Пользователь " + user.getId() + " не прошел валидацию";
            log.warn(errorMessage);
            throw new ValidationException();
        }
        return user;
    }

    public boolean containUser(User user) {
        log.debug("Вызов функции {} с входными данными {}.",
                "containUser", user.getId());
        return users.containsValue(user);
    }

    public boolean containUserId(int id) {
        log.debug("Вызов функции {} с входными данными {}.",
                "containUserId", id);
        return users.containsKey(id);
    }

    public User getUserById(int id) {
        log.debug("Вызов функции {} с входными данными {}.",
                "getUserById", id);
        return users.getOrDefault(id, null);
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
