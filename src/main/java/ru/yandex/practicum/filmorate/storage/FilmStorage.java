package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

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

    void addGenresToFilm(long filmId, TreeSet<Genre> genres);

    void removeGenresFromFilm(long filmId);

    TreeSet<Genre> getFilmGenres(Long filmId);

    List<Film> getPopularFilms(int count);

    void addLike(long userId, long filmId);

    void deleteLike(long userId, long filmId);
}