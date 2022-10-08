package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(long userId, long friendId) {
        if (userStorage.getAllUsers().contains(userStorage.getUser(userId))) {
            if (userStorage.getAllUsers().contains(userStorage.getUser(friendId))) {
                userStorage.getUser(userId).getFriends().add(friendId);
                userStorage.getUser(friendId).getFriends().add(userId);
                log.info("{} и {} подружились",
                        userStorage.getUser(userId).getName(),
                        userStorage.getUser(friendId).getName());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("нет пользователя с id %d", friendId));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", userId));
        }
    }

    public void deleteFriends(long userId, long friendId) {
        if (userStorage.getAllUsers().contains(userStorage.getUser(userId))) {
            if (userStorage.getAllUsers().contains(userStorage.getUser(friendId))) {
                userStorage.getUser(userId).getFriends().remove(friendId);
                userStorage.getUser(friendId).getFriends().remove(userId);
                log.info("{} и {} больше не дружат",
                        userStorage.getUser(userId).getName(),
                        userStorage.getUser(friendId).getName());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("нет пользователя с id %d", friendId));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", userId));
        }
    }

    public List<User> getFriends(long userId) {
        List<User> friends = new ArrayList<>();
        if (userStorage.getAllUsers().contains(userStorage.getUser(userId))) {
            if (!userStorage.getUser(userId).getFriends().isEmpty()) {
                for (long id : userStorage.getUser(userId).getFriends()) {
                    friends.add(userStorage.getUser(id));
                }
                return friends;
            }
            return null;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", userId));
        }
    }

    public Set<User> getCommonFriends(long userId, long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        Set<User> commonFriends = new HashSet<>();

        for (long userFriendId : user.getFriends()) {
            if (friend.getFriends().contains(userFriendId)) {
                User commonFriend = userStorage.getUser(userFriendId);
                commonFriends.add(commonFriend);
            }
        }
        return commonFriends;
    }
}