package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer filmId;

    private Integer assignId() {
        if (filmId == null) {
            filmId = 1;
        } else {
            filmId++;
        }
        return filmId;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        film.setId(assignId());
        if (films.containsValue(film)) {
            throw new ValidationException("такой фильм уже добавлялся");
        }
        FilmValidator.validate(film);
        films.put(film.getId(), film);
        log.info("фильм добавлен", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        FilmValidator.validate(film);
        films.put(film.getId(), film);
        log.info("фильм обновлен", film);
        return film;
    }
}