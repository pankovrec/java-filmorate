package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        UserValidator.validate(user);
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("users")
                .usingColumns("email", "login", "name", "birthday")
                .usingGeneratedKeyColumns("user_id")
                .executeAndReturnKeyHolder(Map.of("email", user.getEmail(),
                        "login", user.getLogin(),
                        "name", user.getName(),
                        "birthday", Date.valueOf(user.getBirthday())))
                .getKeys();
        user.setId((Integer) keys.get("user_id"));
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (getUser(user.getId()) != null) {
            jdbcTemplate.update("UPDATE USERS " +
                            "SET email = ?, login = ?, name = ?, birthday = ? " +
                            "WHERE user_id = ?",
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            return user;
        } else {
            throw new UserNotFoundException("Пользователь с ID=" + user.getId() + " не найден!");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        SqlRowSet usersRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS ");
        while (usersRows.next()) {
            allUsers.add(new User(
                    usersRows.getInt("user_id"),
                    usersRows.getString("email"),
                    usersRows.getString("login"),
                    usersRows.getString("name"),
                    usersRows.getDate("birthday").toLocalDate()));
        }
        return allUsers;
    }

    @Override
    public User getUser(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE user_id = ?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate()
            );
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с id %d не найден", id));
        }
    }

    @Override
    public List<User> getAllFriends(long id) {
        return jdbcTemplate.query("SELECT FRIEND_ID, EMAIL, LOGIN, NAME, BIRTHDAY" +
                " FROM FRIENDS " +
                "JOIN USERS ON FRIENDS.FRIEND_ID = USERS.USER_ID" +
                " WHERE FRIENDS.USER_ID=?", (rs, rowNum) -> new User(
                rs.getLong("friend_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate()), id);
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        String sql = "SELECT friend_id FROM friends WHERE user_id = ? and friend_id " +
                "IN (SELECT friend_id FROM friends WHERE user_id = ?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> findUserIdAndMakeUser(rs), id, otherId);
    }

    private User findUserIdAndMakeUser(ResultSet rs) throws SQLException {
        long userId = rs.getInt("friend_id");
        return getUser(userId);
    }
}