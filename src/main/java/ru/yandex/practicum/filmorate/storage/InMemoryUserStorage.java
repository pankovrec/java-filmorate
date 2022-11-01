package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();
    private Long userId = 0L;

    private Long assignId() {
        userId++;
        return userId;
    }

    @Override
    public User addUser(User user) {
        UserValidator.validate(user);
        user.setId(assignId());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь {}, id={}", user.getName(), user.getId());
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserValidator.validate(user);
        users.put(user.getId(), user);
        log.info("Обновился пользователь {}", user.getName());
        return user;
    }

    @Override
    public User getUser(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с id %d не найден", id));
        }
    }

    @Override
    public void addFriend(long userId, long friendId) {

    }

    @Override
    public void removeFriend(long userId, long friendId) {

    }

    @Override
    public List<User> getAllFriends(long id) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        return null;
    }


}