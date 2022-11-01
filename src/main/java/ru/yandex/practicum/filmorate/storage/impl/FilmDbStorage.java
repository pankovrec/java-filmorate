package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Component("FilmDbStorage")

public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage, MpaStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Film addFilm(Film film) {
        FilmValidator.validate(film);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement
                    ("INSERT INTO FILMS (NAME, DESCRIPTION, RELEASEDATE, DURATION, MPA_ID)" +
                            " VALUES (?, ?, ?, ?, ?)", new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        if (film.getGenres() != null) {
            addGenresToFilm(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update("UPDATE FILMS " +
                        "SET name = ?, description = ?, releaseDate = ?, duration = ?, MPA_ID = ? " +
                        "WHERE film_id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (film.getGenres() != null) {
            removeGenresFromFilm(film.getId());
            addGenresToFilm(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public Film getFilm(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE film_id = ?", id);
        if (userRows.next()) {
            Film film = new Film(
                    userRows.getLong("film_id"),
                    userRows.getString("name"),
                    userRows.getString("description"),
                    userRows.getDate("releaseDate").toLocalDate(),
                    userRows.getInt("duration")
            );
            int mpa = userRows.getInt("mpa_id");
            film.setMpa(mpaStorage.getMpa(mpa));

            Set<Genre> genres = getFilmGenres(id);
            if (genres.size() != 0) {
                film.setGenres(getFilmGenres(id));
            }
            return film;
        } else {
            throw new FilmNotFoundException("Фильм с ID=" + getFilm(id) + " не найден!");
        }
    }

    public List<Film> getAllFilms() {
        List<Film> allFilms = new ArrayList<>();
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("SELECT * FROM films");

        while (filmsRows.next()) {
            Film film = new Film(filmsRows.getInt("film_id"),
                    filmsRows.getString("name"),
                    filmsRows.getString("description"),
                    filmsRows.getDate("releasedate").toLocalDate(),
                    filmsRows.getInt("duration"));
            film.setMpa(
                    mpaStorage.getMpa(
                            filmsRows.getInt("mpa_id")));
            film.setGenres(
                    getFilmGenres(film.getId())
            );

            allFilms.add(film);
        }
        return allFilms;
    }

    @Override
    public void addGenresToFilm(long filmId, TreeSet<Genre> genres) {
        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO FILMS_GENRE (film_id, genre_id) VALUES (?,?)", filmId, genre.getId());
        }
    }

    @Override
    public void removeGenresFromFilm(long filmId) {
        jdbcTemplate.update("DELETE FROM FILMS_GENRE WHERE FILM_ID=?", filmId);
    }

    @Override
    public TreeSet<Genre> getFilmGenres(Long filmId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT GENRES.GENRE_ID, NAME " +
                "FROM FILMS_GENRE " +
                "JOIN GENRES ON FILMS_GENRE.GENRE_ID = GENRES.GENRE_ID " +
                "WHERE FILMS_GENRE.FILM_ID=?", filmId);

        TreeSet<Genre> genres = new TreeSet<>();

        while (sqlRowSet.next()) {
            genres.add(new Genre(sqlRowSet.getInt("genre_id"),
                    sqlRowSet.getString("name")));
        }
        return genres;
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sql = "MERGE INTO likes KEY(film_id, user_id) VALUES (?, ?);";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ?;", filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = new ArrayList<>();
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet
                ("SELECT FILMS.FILM_ID,NAME,DESCRIPTION,RELEASEDATE,DURATION, MPA_ID, COUNT(LIKES.user_id) " +
                        "FROM FILMS " +
                        "LEFT JOIN likes ON FILMS.FILM_ID = LIKES.FILM_ID " +
                        "GROUP BY films.FILM_ID " +
                        "ORDER BY count(LIKES.USER_ID) DESC " +
                        "LIMIT ?", count);

        while (filmsRows.next()) {
            Film film = new Film(filmsRows.getInt("film_id"),
                    filmsRows.getString("name"),
                    filmsRows.getString("description"),
                    filmsRows.getDate("releaseDate").toLocalDate(),
                    filmsRows.getInt("duration"));
            film.setMpa(
                    mpaStorage.getMpa(
                            filmsRows.getInt("mpa_id")));
            film.setGenres(
                    getFilmGenres(film.getId())
            );

            popularFilms.add(film);
        }
        return popularFilms;
    }
}