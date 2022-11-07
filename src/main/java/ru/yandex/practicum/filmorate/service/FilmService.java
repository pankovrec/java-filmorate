package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage,
                       @Qualifier("LikeDbStorage") LikeStorage likeStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(long filmId, long userId) {
        filmAndUserExistValid(filmId, userId);
        likeStorage.addLike(Like
                .builder()
                .filmId(filmId)
                .userId(userId)
                .build());
    }

    public void deleteLike(long filmId, long userId) {
        filmAndUserExistValid(filmId, userId);
        likeStorage.deleteLike(Like
                .builder()
                .filmId(filmId)
                .userId(userId)
                .build());
    }

    public List<Film> popularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        FilmValidator.validate(film);
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (filmExistValid(film.getId())) {
            throw new FilmNotFoundException("this film not exist");
        }
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(long id) {
        if (filmExistValid(id)) {
            throw new FilmNotFoundException("this film not exist");
        }
        return filmStorage.getFilm(id);
    }

    private boolean filmAndUserExistValid(long filmId, long userId) {
        filmExistValid(filmId);
        if (!userStorage.getAllUsers().contains(userStorage.getUser(userId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", userId));
        }
        return false;
    }

    private boolean filmExistValid(long filmId) {
        if (!filmStorage.getAllFilms().contains(filmStorage.getFilm(filmId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет фильма с id %d", filmId));
        }
        return false;
    }
}
