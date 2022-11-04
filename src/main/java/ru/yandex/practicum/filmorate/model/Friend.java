package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friend {
    private long userId;
    private long friendId;
    private int status;

    public Friend(long userId, long friendId, int status) {
        this.userId = userId;
        this.friendId = friendId;
        this.status = status;
    }
}
