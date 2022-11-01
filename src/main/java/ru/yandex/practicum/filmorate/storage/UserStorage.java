package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Интерфейс user storage.
 */
public interface UserStorage {

    /**
     * добавить пользователя
     *
     * @return
     */
    User addUser(User user);

    /**
     * получить список пользователей
     *
     * @return
     */
    Collection<User> getAllUsers();

    /**
     * обновить пользователя
     * по id
     *
     * @return
     */
    User updateUser(User user);

    /**
     * получить пользователя
     * по id
     *
     * @return
     */
    User getUser(long id);

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    List<User> getAllFriends(long id);

    List<User> getCommonFriends(long userId, long friendId);

}