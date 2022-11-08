package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Builder
@Getter
@Setter
public class Like {
    private long filmId;
    private long userId;
}
