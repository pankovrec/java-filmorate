package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

@Component("FriendDbStorage")
@RequiredArgsConstructor


public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Friend friend) {
        jdbcTemplate.update("MERGE INTO friends KEY(user_id, friend_id, status) VALUES (?, ?, ?)",
                friend.getUserId(), friend.getFriendId(), 1);
    }

    @Override
    public void removeFriend(Friend friend) {

        jdbcTemplate.update("DELETE FROM friends WHERE user_id=? AND friend_id=?", friend.getUserId(), friend.getFriendId());
    }
}
