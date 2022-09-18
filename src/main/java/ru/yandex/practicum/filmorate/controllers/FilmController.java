package ru.yandex.practicum.filmorate.controllers;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @NonNull @RequestBody Film film) throws ValidationException {
        film.setId(++filmId);
        FilmValidator.validate(film);
        films.put(film.getId(), film);
        log.info("фильм добавлен", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @NonNull @Valid Film film) throws ValidationException {
        FilmValidator.validate(film);
        films.put(film.getId(), film);
        log.info("фильм обновлен", film);
        return film;
    }
}