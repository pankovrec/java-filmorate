package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Component("LikeDbStorage")
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Like like) {
        jdbcTemplate.update("MERGE INTO likes KEY(film_id, user_id) VALUES (?, ?);",
                like.getFilm_id(), like.getUser_id());
    }

    @Override
    public void deleteLike(Like like) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ?;",
                like.getFilm_id(), like.getUser_id());
    }
}
