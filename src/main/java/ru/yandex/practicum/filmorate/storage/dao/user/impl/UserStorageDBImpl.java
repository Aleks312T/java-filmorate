package ru.yandex.practicum.filmorate.storage.dao.user.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.user.interf.UserStorageDB;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Repository
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

    @Override
    public User updateUser(User user) {
        log.trace("Изменение пользователя");
        String sql = "UPDATE users SET login = ?, userName = ?, email = ?, birthday = ? WHERE userId = ?";
        jdbcTemplate.update(sql, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        return getUser(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        log.trace("Получение всех пользователей");
        String sql = "SELECT * FROM users";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql);
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

    public User addFriend(Integer userId, Integer friendId) {
        User friend = getUser(friendId);
        List<User> userFriends = getFriends(userId);
        if (userFriends == null || !userFriends.contains(friend)) {
            String sqlCheckPair = "SELECT FRIENDSHIPSTATUSID = ? " +
                                  "FROM friends " +
                                  "WHERE userId = ?" +
                                  "AND friendId = ?";
            //Проверяем есть ли они в друзьях в другую сторону
            SqlRowSet checkPairResult = jdbcTemplate.queryForRowSet(sqlCheckPair, 1, friendId, userId);
            if (checkPairResult.next()) {
                String sql = "INSERT INTO friends (userId, friendId, friendshipStatusId) "
                        + "VALUES(?, ?, ?)";
                jdbcTemplate.update(sql, userId, friendId, 1);
                jdbcTemplate.update(sql, friendId, userId, 1);
                return getUser(userId);
            } else {
                String sql = "INSERT INTO friends (userId, friendId, friendshipStatusId) "
                        + "VALUES(?, ?, ?)";
                jdbcTemplate.update(sql, userId, friendId, 2);
                return getUser(userId);
            }
        } else {
            throw new InputMismatchException("Пользователь с id:" + friendId
                    + "уже добавлен в список друзей пользователя с id:" + userId);
        }
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        User friend = getUser(friendId);
        List<User> userFriends = getFriends(userId);
        if (userFriends == null) {
            String errorMessage = "У пользователя с Id " + userId + " нет друзей.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
        if (userFriends.contains(friend)) {
            String sql = "DELETE FROM FRIENDS WHERE userId = ? AND friendId = ?";
            jdbcTemplate.update(sql, userId, friendId);
            return getUser(userId);
        } else {
            String errorMessage = "У пользователя с Id " + userId + " нет друга с Id " + friendId + ".";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        }
    }

    public List<User> getFriends(Integer userId) {
        List<User> friends = new ArrayList<>();
        String sqlQuery = "SELECT * FROM users "
                + "WHERE users.userId IN "
                    + "(SELECT friendId from friends "
                    + "WHERE userId = ?)";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        while (srs.next()) {
            friends.add(userMap(srs));
        }
        return friends;
    }

    public Collection<User> getCommonFriends(Integer firstUserId, Integer secondUserId) {
        List<User> commonFriends = new ArrayList<>();
        String sqlQuery = "SELECT * FROM users "
                        + "WHERE users.userId IN ("
                            + "SELECT friendId from friends "
                            + "WHERE userId IN (?, ?) "
                            + "AND friendId NOT IN (?, ?))";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, firstUserId, secondUserId, firstUserId, secondUserId);
        while (srs.next()) {
            commonFriends.add(userMap(srs));
        }
        return commonFriends;
    }

    private int getNewUserId() {
        String sqlGetId = "SELECT userId \n" +
                "FROM USERS \n" +
                "ORDER BY userId DESC \n" +
                "LIMIT 1";

        List<Integer> id = jdbcTemplate.query(sqlGetId, (rs, rowNum) -> rs.getInt("userId"));
        return id.get(0);
    }

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
