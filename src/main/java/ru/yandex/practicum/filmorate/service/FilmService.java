package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long filmId, long userId) {
        if (filmStorage.getAllFilms().contains(filmStorage.getFilm(filmId))) {
            if (userStorage.getAllUsers().contains(userStorage.getUser(userId))) {
                filmStorage.getFilm(filmId).getLikes().add((int) userId);
                filmStorage.getFilm(filmId).setLikesCount(filmStorage.getFilm(filmId).getLikes().size());
                log.info("пользователю {} понравился фильм {}", userStorage.getUser(userId).getEmail(),
                        filmStorage.getFilm(filmId).getName());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("нет пользователя с id %d", userId));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет фильма с id %d", filmId));
        }
    }

    public void deleteLike(long filmId, long userId) {
        if (filmStorage.getAllFilms().contains(filmStorage.getFilm(filmId))) {
            if (userStorage.getAllUsers().contains(userStorage.getUser(userId))) {
                filmStorage.getFilm(filmId).getLikes().remove(userId);
                filmStorage.getFilm(filmId).setLikesCount(filmStorage.getFilm(filmId).getLikes().size());
                log.info("Пользователю {} больше не нравится фильм {}", userStorage.getUser(userId).getEmail(),
                        filmStorage.getFilm(filmId).getName());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("нет пользователя с id %d", userId));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет фильма с id %d", filmId));
        }
    }

    public List<Film> popularFilms(long count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .distinct()
                .limit(count)
                .collect(Collectors.toList());
    }
}