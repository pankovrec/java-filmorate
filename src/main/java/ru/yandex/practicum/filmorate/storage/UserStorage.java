package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

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

    List<User> getAllFriends(long id);

    List<User> getCommonFriends(long userId, long friendId);
}