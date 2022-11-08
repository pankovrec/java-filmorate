package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

public interface LikeStorage {
    void addLike(Like like);

    void deleteLike(Like like);
}
