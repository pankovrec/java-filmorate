package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {
    public static void validate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("name: не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина description — 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("releaseDate: должен быть не раньше 28 декабря 1895 года");
        }

        if (film.getDuration() < 0) {
            throw new ValidationException("duration: должна быть положительной");
        }
        if (film.getMpa() == null) {
            throw new ValidationException("Не прошла валидация MPA");
        }
    }
}
