package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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

    /**
     * добавление жанров к фильмам
     *
     * @return
     */
    void addGenresToFilm(long filmId, Set<Genre> genres);

    /**
     * удаление жанров фильма
     *
     * @return
     */
    void removeGenresFromFilm(long filmId);

    /**
     * получение жанров фильма
     *
     * @return
     */
    Set<Genre> getFilmGenres(Long filmId);

    /**
     * получение популярных фильмов
     *
     * @return
     */
    List<Film> getPopularFilms(int count);

}