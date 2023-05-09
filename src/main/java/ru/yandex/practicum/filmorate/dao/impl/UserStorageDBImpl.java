package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.UserStorageDB;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        log.debug("Пользователь {} добавлен", user.getId());
        return user;
    }

    //TODO: нужно будет проверить
    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET login = ?, userName = ?, email = ?, birthday = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    //TODO: нужно будет доделать
    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        List<User> users = new ArrayList<>();
        while (srs.next()) {
            users.add(userMap(srs));
        }
        return users;
    }

    @Override
    public User getUser(int id) {
        log.trace("Получение пользователя");
        String sql = "SELECT * FROM users WHERE USERID=?";
        SqlRowSet users = jdbcTemplate.queryForRowSet(sql, id);
        if (users.next()) {
            return userMap(users);
        } else {
            String errorMessage = "Пользователя с Id " + id + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        }
    }

    private int getNewUserId() {
        String sqlGetId = "SELECT userId \n" +
                "FROM USERS \n" +
                "ORDER BY userId DESC \n" +
                "LIMIT 1";

        List<Integer> id = jdbcTemplate.query(sqlGetId, (rs, rowNum) -> rs.getInt("userId"));
        return id.get(0);
    }

//    private User makeUser(ResultSet userRows) throws SQLException {
//        User user = User.builder()
//                .id(userRows.getInt("userId"))
//                .login(userRows.getString("login"))
//                .email(userRows.getString("email"))
//                .birthday(userRows.getObject("birthday", LocalDate.class))
//                .build();
//
//        if (userRows.getString("userName").isBlank()) {
//            user.setName(user.getLogin());
//        } else {
//            user.setName(userRows.getString("userName"));
//        }
//        //TODO: возможно, нужно будет доделать
//        return user;
//    }

    private static User userMap(SqlRowSet srs) {
        Integer id = srs.getInt("userId");
        String name = srs.getString("userName");
        String login = srs.getString("login");
        String email = srs.getString("email");
        LocalDate birthday = Objects.requireNonNull(srs.getTimestamp("birthday"))
                .toLocalDateTime().toLocalDate();
        return User.builder()
                .id(id)
                .name(name)
                .login(login)
                .email(email)
                .birthday(birthday)
                .build();
    }
}
