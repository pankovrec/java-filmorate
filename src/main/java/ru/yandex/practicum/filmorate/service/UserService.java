package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(long userId, long friendId) {
        if (!userStorage.getAllUsers().contains(userStorage.getUser(userId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", friendId));
        }
        if (!userStorage.getAllUsers().contains(userStorage.getUser(friendId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", userId));
        } else {
            userStorage.getUser(userId).getFriends().add(friendId);
            userStorage.getUser(friendId).getFriends().add(userId);
            log.info("{} и {} подружились",
                    userStorage.getUser(userId).getName(),
                    userStorage.getUser(friendId).getName());
        }
    }

    public void deleteFriends(long userId, long friendId) {
        if (!userStorage.getAllUsers().contains(userStorage.getUser(userId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", userId));
        }
        if (!userStorage.getAllUsers().contains(userStorage.getUser(friendId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", userId));
        } else {
            userStorage.getUser(userId).getFriends().remove(friendId);
            userStorage.getUser(friendId).getFriends().remove(userId);
            log.info("{} и {} больше не дружат",
                    userStorage.getUser(userId).getName(),
                    userStorage.getUser(friendId).getName());
        }
    }

    public List<User> getFriends(long userId) {
        return userStorage.getUser(userId).getFriends().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    public Set<User> getCommonFriends(long userId, long friendId) {
        return userStorage.getUser(userId).getFriends().stream()
                .filter(u -> userStorage.getUser(friendId).getFriends().contains(u))
                .map(userStorage::getUser)
                .collect(Collectors.toSet());
    }

    public User addUser(User user) {
        userStorage.addUser(user);
        return user;
    }

    public User updateUser(User user) {
        userStorage.updateUser(user);
        return user;
    }

    public User getUser(long id) {
        return userStorage.getUser(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }
}