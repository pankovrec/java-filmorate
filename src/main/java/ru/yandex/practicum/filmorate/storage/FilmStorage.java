package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

/**
 * Интерфейс film storage.
 */
public interface FilmStorage {

    /**
     * добавить фильм
     *
     * @return
     */
    Film addFilm(Film film);

    /**
     * получение фильма по id
     *
     * @return
     */
    Film updateFilm(Film film);

    /**
     * получение фильма по id
     *
     * @return
     */
    Film getFilm(long id);

    /**
     * Список всех фильмов
     *
     * @return
     */
    Collection<Film> getAllFilms();
}