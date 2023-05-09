package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.UserStorageDB;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UserStorageDBImpl implements UserStorageDB {
    private static final Logger log = LoggerFactory.getLogger(UserStorageDBImpl.class);
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        log.trace("Добавление нового пользователя");
        //Проверка происходит на уровне сервиса, поэтому здесь работаем как есть.
        String sql = "INSERT INTO users (login, userName, email, birthday) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
        user.setId(getNewUserId());
//        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql);
//        jdbcTemplate.execute(sql);
        return user;
    }

    @Override
    public User updateUser(User user) {
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    private int getNewUserId() {
        String sqlGetId = "SELECT userId \n" +
                "FROM USERS \n" +
                "ORDER BY userId DESC \n" +
                "LIMIT 1";

        List<Integer> id = jdbcTemplate.query(sqlGetId, (rs, rowNum) -> rs.getInt("userId"));
        return id.get(0);
    }
}
