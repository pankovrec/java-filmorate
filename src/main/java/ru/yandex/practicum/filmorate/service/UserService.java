package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public void addFriends(long userId, long friendId) {
        friendStorage.addFriend(Friend
                .builder()
                .userId(userId)
                .friendId(friendId)
                .build());
    }

    public void deleteFriends(long userId, long friendId) {
        friendStorage.removeFriend(Friend
                .builder()
                .userId(userId)
                .friendId(friendId)
                .build());
    }

    public List<User> getFriends(long userId) {
        return userStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        return userStorage.getCommonFriends(userId, friendId);
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