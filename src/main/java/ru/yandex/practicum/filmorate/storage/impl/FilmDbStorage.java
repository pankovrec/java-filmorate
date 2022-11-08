package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Component("FilmDbStorage")

public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        return getFilm(film.getId());
    }

    @Override
    public Film getFilm(long id) {

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS as f " +
                "LEFT JOIN MPA_RATING on f.mpa_id = mpa_rating.mpa_id WHERE film_id = ?", id);
        if (userRows.next()) {
            Film film = new Film(
                    userRows.getLong("film_id"),
                    userRows.getString("name"),
                    userRows.getString("description"),
                    userRows.getDate("releaseDate").toLocalDate(),
                    userRows.getInt("duration"));

            int mpa = userRows.getInt("mpa_id");
            String mpaName = userRows.getString("mpa_name");
            film.setMpa(new Mpa(mpa, mpaName));
            Set<Genre> genres = getFilmGenres(id);
            if (genres.size() != 0) {
                film.setGenres(getFilmGenres(id));
            }
            return film;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Фильм с id %d не найден", id));
        }
    }

    public List<Film> getAllFilms() {
        List<Film> allFilms = new ArrayList<>();
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS " +
                "LEFT JOIN MPA_RATING on films.mpa_id = mpa_rating.mpa_id");

        while (filmsRows.next()) {
            Film film = new Film(filmsRows.getInt("film_id"),
                    filmsRows.getString("name"),
                    filmsRows.getString("description"),
                    filmsRows.getDate("releasedate").toLocalDate(),
                    filmsRows.getInt("duration"));

            int mpa = filmsRows.getInt("mpa_id");
            String mpaName = filmsRows.getString("mpa_name");
            film.setMpa(new Mpa(mpa, mpaName));
            film.setGenres(
                    getFilmGenres(film.getId())
            );
            allFilms.add(film);
        }
        return allFilms;
    }

    @Override
    public void addGenresToFilm(long filmId, Set<Genre> genres) {
        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO FILMS_GENRE (film_id, genre_id) VALUES (?,?)", filmId, genre.getId());
        }
    }

    @Override
    public void removeGenresFromFilm(long filmId) {
        jdbcTemplate.update("DELETE FROM FILMS_GENRE WHERE FILM_ID=?", filmId);
    }

    @Override
    public Set<Genre> getFilmGenres(Long filmId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT GENRES.GENRE_ID, NAME " +
                "FROM FILMS_GENRE " +
                "JOIN GENRES ON FILMS_GENRE.GENRE_ID = GENRES.GENRE_ID " +
                "WHERE FILMS_GENRE.FILM_ID=?", filmId);

        Set<Genre> genres = new TreeSet<>();

        while (sqlRowSet.next()) {
            genres.add(new Genre(sqlRowSet.getInt("genre_id"),
                    sqlRowSet.getString("name")));
        }
        return genres;
    }


    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = new ArrayList<>();
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet
                ("SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASEDATE, F.DURATION, " +
                        "F.MPA_ID, COUNT(LIKES.USER_ID), MPA_NAME " +
                        "FROM FILMS AS F LEFT JOIN LIKES ON F.FILM_ID = LIKES.FILM_ID " +
                        "JOIN MPA_RATING ON F.MPA_ID = MPA_RATING.MPA_ID " +
                        "GROUP BY F.FILM_ID ORDER BY COUNT(LIKES.USER_ID) DESC LIMIT ?", count);

        while (filmsRows.next()) {
            Film film = new Film(filmsRows.getInt("film_id"),
                    filmsRows.getString("name"),
                    filmsRows.getString("description"),
                    filmsRows.getDate("releaseDate").toLocalDate(),
                    filmsRows.getInt("duration"));
            int mpa = filmsRows.getInt("mpa_id");
            String mpaName = filmsRows.getString("mpa_name");

            film.setMpa(new Mpa(mpa, mpaName));
            film.setGenres(
                    getFilmGenres(film.getId())
            );

            popularFilms.add(film);
        }
        return popularFilms;
    }
}