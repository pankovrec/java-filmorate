package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Like {
    private long film_id;
    private long user_id;

    public Like(long film_id, long user_id) {
        this.film_id = film_id;
        this.user_id = user_id;
    }
}
